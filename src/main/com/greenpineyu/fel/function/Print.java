package com.greenpineyu.fel.function;

import org.antlr.runtime.tree.Tree;

import com.greenpineyu.fel.common.ObjectUtils;
import com.greenpineyu.fel.compile.FelMethod;
import com.greenpineyu.fel.compile.SourceBuilder;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.parser.FelNode;

public class Print implements Function{

	@Override
	public Object call(FelNode node, FelContext context) {
		if(node.getChildCount()>0){
			Tree child = node.getChild(0);
			Object arg = TolerantFunction.eval(context, child);
			ObjectUtils.print(arg);
		}
		return SourceBuilder.VOID;
	}
	
	@Override
	public String getName() {
		return "print";
	}

	@Override
	public SourceBuilder toMethod(FelNode node, FelContext ctx) {
        String src = null;
        FelNode child = null;
		if(node.getChildCount()>0){
			child = (FelNode) node.getChild(0);
		}
		if(child!=null){
			src =  "ObjectUtils.print("+child.toMethod(ctx).source(ctx, child)+")";
		}else{
			src = "ObjectUtils.print(\"\")";
		}
		return new FelMethod(SourceBuilder.VOID.getClass(),src);
	}

}
