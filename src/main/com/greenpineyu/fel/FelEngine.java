package com.greenpineyu.fel;

import java.util.Map;

import com.greenpineyu.fel.compile.CompileService;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.event.EventMgr;
import com.greenpineyu.fel.exception.ExceptionHandler;
import com.greenpineyu.fel.function.FunMgr;
import com.greenpineyu.fel.function.Function;
import com.greenpineyu.fel.optimizer.Optimizer;
import com.greenpineyu.fel.parser.FelNode;
import com.greenpineyu.fel.parser.Parser;
import com.greenpineyu.fel.security.SecurityMgr;

/**
 * 表达式引擎
 * 
 * @author yqs
 */
public interface FelEngine {

	/**
	 * 默认实例
	 */
	FelEngine instance = new FelEngineImpl();

	/**
	 * 执行表达式，获取结果
	 * 
	 * @param exp
	 * @return
	 */
	Object eval(String exp);

	/**
	 * 使用指定的引擎上下文执行表达式，获取结果
	 * 
	 * @param exp
	 * @param ctx 引擎上下文
	 * @return
	 */
	Object eval(String exp, FelContext ctx);
	
	/**
	 * 使用指定的Map执行表达式，获取结果
	 * @param exp
	 * @param varMap
	 * @return
	 */
	Object eval(String exp, Map<String, Object> varMap);

	/**
	 * 解析表达式为节点
	 * 
	 * @param exp
	 * @return AST节点
	 */
	FelNode parse(String exp);

	/**
	 * 编译表达式
	 * 
	 * @param exp 表达式
	 * @param ctx 引擎上下文
	 * @param opts 编译优化选项
	 * @return 可执行的表达式对象
	 */
	Expression compile(String exp, FelContext ctx, Optimizer... opts);
	
	/**
	 * 编译表达式
	 * 
	 * @param exp 表达式
	 * @param varMap 变量map
	 * @return 可执行的表达式对象
	 */
	Expression compile(String exp, Map<String, Object> varMap);

	/**
	 * 编译表达式
	 * @param exp
	 * @return
	 */
	Expression compile(String exp);

	/**
	 * @return 引擎执行环境
	 */
	FelContext getContext();

	/**
	 * 添加函数到用户函数库中（执行表达式时，优先从用户函数库中获取函数）
	 * 
	 * @param fun 要添加的函数
	 */
	void addFun(Function fun);

	/**
	 * 获取编译器
	 * @return
	 */
	CompileService getCompiler() ;

	/**
	 * 设置编译器
	 * @param compiler
	 */
	void setCompiler(CompileService compiler);

	/**
	 * 获取解析器
	 * @return
	 */
	Parser getParser();

	/**
	 * 设置解析器
	 * @param parser
	 */
	void setParser(Parser parser);

	/**
	 * 获取函数管理器
	 * @return
	 */
	FunMgr getFunMgr();

	/**
	 * 设置函数管理器
	 * @param funMgr
	 */
	void setFunMgr(FunMgr funMgr);

	/**
	 * 设置Context
	 * @param context
	 */
	void setContext(FelContext context);

	/**
	 * 返回安全管理器
	 * @return
	 */
	SecurityMgr getSecurityMgr();

	/**
	 * 设置安全管理器
	 * @param mgr
	 */
	void setSecurityMgr(SecurityMgr mgr);

	/**
	 * 获取事件管理器
	 * @return
	 */
	EventMgr getEventMgr();

	/**
	 * 设置事件管理器
	 * @param mgr
	 */
	void setEventMgr(EventMgr mgr);

	/**
	 * 添加异常处理机制
	 * @param handler
	 */
	void setExceptionHandler(ExceptionHandler handler);

	/**
	 * 移除异常处理机制
	 */
	void removeExceptionHandler();

	

}
