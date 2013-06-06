package com.greenpineyu.fel;

import java.util.Map;

import com.greenpineyu.fel.common.FelBuilder;
import com.greenpineyu.fel.compile.CompileService;
import com.greenpineyu.fel.context.ArrayCtxImpl;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.ReadOnlyMapContext;
import com.greenpineyu.fel.context.Var;
import com.greenpineyu.fel.event.Event;
import com.greenpineyu.fel.event.EventListener;
import com.greenpineyu.fel.event.EventMgr;
import com.greenpineyu.fel.event.Events;
import com.greenpineyu.fel.event.ExceptionEvent;
import com.greenpineyu.fel.exception.ExceptionHandler;
import com.greenpineyu.fel.function.FunMgr;
import com.greenpineyu.fel.function.Function;
import com.greenpineyu.fel.optimizer.Optimizer;
import com.greenpineyu.fel.optimizer.VarVisitOpti;
import com.greenpineyu.fel.parser.AntlrParser;
import com.greenpineyu.fel.parser.FelNode;
import com.greenpineyu.fel.parser.Parser;
import com.greenpineyu.fel.security.SecurityMgr;

/**
 * 执行引擎
 * 
 * @author yqs
 * 
 */
public class FelEngineImpl implements FelEngine {

	private FelContext context;

	private CompileService compiler;

	private Parser parser;
	
	private FunMgr funMgr;
	
	private SecurityMgr securityMgr;

	private EventMgr eventMgr;

	public EventMgr getEventMgr() {
		return eventMgr;
	}

	public SecurityMgr getSecurityMgr() {
		return securityMgr;
	}

	public void setSecurityMgr(SecurityMgr securityMgr) {
		this.securityMgr = securityMgr;
	}

	public FelEngineImpl(FelContext context) {
		this.context = context;
		compiler = new CompileService();
		parser = new AntlrParser(this);
		this.funMgr=new FunMgr();
	}
	
	{
		this.eventMgr = FelBuilder.newEventMgr();
		this.securityMgr = FelBuilder.newSecurityMgr();
	}

	public FelEngineImpl() {
		this(newContext());
		// this(new MapContext());
	}

	private static ArrayCtxImpl newContext() {
		return new ArrayCtxImpl();
	}

	@Override
	public FelNode parse(String exp) {
		return parser.parse(exp);
	}

	@Override
	public Object eval(String exp) {
		return this.eval(exp, this.context);
	}

	public Object eval(String exp, Var... vars) {
		try {
			FelNode node = parse(exp);
			Optimizer opt = new VarVisitOpti(vars);
			node = opt.call(context, node);
			return node.eval(context);
		} catch (Exception e) {
			return this.eventMgr.onEvent(new ExceptionEvent(Events.EXCEPTION, exp, null, null));
		}
	}

	@Override
	public Object eval(String exp, FelContext ctx) {
		try {
			return parse(exp).eval(ctx);
		} catch (Exception e) {
			ExceptionEvent event = new ExceptionEvent(Events.EXCEPTION, exp, ctx, null);
			event.setException(e);
			return this.eventMgr.onEvent(event);
		}
	}
	@Override
	public Object eval(String exp, Map<String,Object> varMap) {
		FelContext ctx = null;
		try {
			ctx= getContext(varMap);
			return parse(exp).eval(ctx);
		} catch (Exception e) {
			ExceptionEvent event = new ExceptionEvent(Events.EXCEPTION, exp, ctx, null);
			event.setException(e);
			return this.eventMgr.onEvent(event);
		}
	}


	public Expression compile(String exp, Var... vars) {
		return compile(exp, (FelContext)null, new VarVisitOpti(vars));
	}

	@Override
	public Expression compile(String exp) {
		return compile(exp, (FelContext) null);
	}

	public Expression compile(final String exp, Map<String, Object> varMap) {
		FelContext ctx = getContext(varMap);
		return compile(exp,ctx);
	}

	public Expression compile(final String exp, FelContext ctx, Optimizer... opts) {
		if (ctx == null) {
			ctx = this.context;
		}
		FelNode node = parse(exp);
		if (opts != null) {
			for (Optimizer opt : opts) {
				if (opt != null) {
					node = opt.call(ctx, node);
				}
			}
		}
		final Expression exception = compiler.compile(ctx, node, exp);
		EventListener<Event> listener = this.eventMgr.getListener(Events.EXCEPTION);
		if (listener == null) {
			return exception;
		}
		return new Expression() {

			@Override
			public Object eval(FelContext context) {
				try {
					return exception.eval(context);
				} catch (Exception e) {
					return eventMgr.onEvent(new ExceptionEvent(Events.EXCEPTION, exp, context, null));
				}
			}
		};
	}

	@Override
	public String toString() {
		return "FelEngine";
	}

	@Override
	public void addFun(Function fun) {
		this.funMgr.add(fun);
	}
	
	@Override
	public FelContext getContext() {
		return this.context;
	}
	
	@Override
	public CompileService getCompiler() {
		return compiler;
	}


	@Override
	public void setCompiler(CompileService compiler) {
		this.compiler = compiler;
	}


	@Override
	public Parser getParser() {
		return parser;
	}


	@Override
	public void setParser(Parser parser) {
		this.parser = parser;
	}


	@Override
	public FunMgr getFunMgr() {
		return funMgr;
	}


	@Override
	public void setFunMgr(FunMgr funMgr) {
		this.funMgr = funMgr;
	}


	@Override
	public void setContext(FelContext context) {
		this.context = context;
	}

	@Override
	public void setEventMgr(EventMgr mgr) {
		this.eventMgr = mgr;

	}

	@Override
	public void setExceptionHandler(final ExceptionHandler handler) {
		this.eventMgr.addListener(new EventListener<ExceptionEvent>() {

			@Override
			public Object onEvent(ExceptionEvent event) {
				return handler.onException(event.getException(), event.getExpression(), event.getContext());
			}

			@Override
			public String getId() {
				return Events.EXCEPTION;
			}
		});
	}
	
	private FelContext getContext(Map<String, Object> varMap) {
		return new ReadOnlyMapContext(varMap);
	}

	@Override
	public void removeExceptionHandler() {
		this.eventMgr.removeListenerById(Events.EXCEPTION);
	}
	
    

}
