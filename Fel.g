grammar Fel;

options {
 backtrack=true;
 memoize=true;
 output=AST;
 ASTLabelType=CommonTree;
 language=Java;
 superClass=Parser;
}

@header {
package com.greenpineyu.fel.parser;
//import com.greenpineyu.fel.exception.ParseException;
import org.antlr.runtime.Parser;
}

@lexer::header {package com.greenpineyu.fel.parser;}  

@members{
protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow)
	throws RecognitionException
{
	throw new MismatchedTokenException(ttype, input);
}
//public void emitErrorMessage(String msg) {
//		System.err.println(msg);
	// do not display error ,instead of ErrorHndler
//}

}

@lexer::members {
public void reportError(RecognitionException e){
    throw new RuntimeException( e.toString());
}

}

@rulecatch {
catch (RecognitionException e) {
reportError(e); 
throw e;
}
}
program : conditionalExpression EOF!;


parExpression
    :   '('! expressionList ')'!
    ;

expressionList
    :   conditionalExpression?
   (','! conditionalExpression? )*    
    ;     
conditionalExpression
    :   conditionalOrExpression ( Ques^ conditionalExpression Colon! conditionalExpression )?
    ;
conditionalOrExpression
	: conditionalAndExpression (Or^ conditionalAndExpression)*
	;
	
conditionalAndExpression
	: equalityExpression (And^ equalityExpression)*
	;

equalityExpression
    :   relationalExpression ( Equals^ relationalExpression )*
    ;

relationalExpression
    :   additiveExpression ( Relational^ additiveExpression )*
    ;

additiveExpression
    :   multiplicativeExpression (Additive^ multiplicativeExpression )*
    ;

multiplicativeExpression
    :   unaryExpression ( Multiplicative^ unaryExpression )*
    ;

unaryExpression
      : Additive^ unaryExpression
	|unaryExpressionNotPlusMinus
	;

unaryExpressionNotPlusMinus
    :  Not^ unaryExpressionNotPlusMinus
   // |   dotExpression
    |   array (Dot^ parDot )*  //  |   parExpression
  ;
  array	
  :  primary (Bracket^ conditionalExpression ']'!)*
  ;
    primary 
    :   parExpression          
    |   parDot (Dot^ parDot )*
    | literal
    //|   Identifier  (Dot^ Identifier )*      //  (identifierSuffix        )?
    ;
    /*
    selector  
    :   Dot^ Identifier  (arguments   )?
    |   '[' conditionalExpression ']'
    ;
    */
    
    parDot
    :	identifierOrFun //(Bracket^ conditionalExpression ']')*
    |	StringLiteral
    |	CharacterLiteral
    ;
   
    identifierOrFun
     :	Identifier     
     |  Identifier^ arguments
     {
     	if($arguments.text!=null){
	  root_0 = new com.greenpineyu.fel.parser.FunNode(root_0);
      	}
     }
     ;
    
    arguments
    :   '('! expressionList? ')'!
    ;
     
    
   
 /*    
dotExpressiliteralon
	:
	   literal  (Dot^ selector)*
	|  primary  (Dot^ primary)+
	//|  funExpression (Dot^ conditionalExpression)+
	;
    
   funExpression
    :Identifier^ arguments
     {
     	if($arguments.text!=null){
	    //root_0 = new com.greenpineyu.fel.parser.FunNode(root_0);
      	}
     }
    ;  
primary
    	:	
    Identifier
    | funExpression
    ;

selector
    :Identifier^ arguments?
     {
     	if($arguments.text!=null){
	    //root_0 = new com.greenpineyu.fel.parser.FunNode(root_0);
      	}
     }
    ;*/

literal 
    :   integerLiteral
    |   FloatingPointLiteral
    |   CharacterLiteral
    |   StringLiteral
    |   BooleanLiteral
    ;    
integerLiteral
    :   HexLiteral
    |   OctalLiteral
    |   DecimalLiteral
    ;
    
// LEXER

Dot	:	'.';

Or	:	'||';

And	:	'&&';

Not	:	'!';

Ques	:	'?';

Bracket :	'[';

Colon	:	':';

Relational :	'>'|'<'|'<='|'>=';

Equals :	'=='|'!=';

Multiplicative :	'*'|'/'|'%';

Additive :	('+'|'-');
	
HexLiteral :  '0' ('x'|'X') HexDigit+ IntegerTypeSuffix? ;

DecimalLiteral : ('0' | '1'..'9' '0'..'9'*) IntegerTypeSuffix? ;

OctalLiteral :  '0' ('0'..'7')+ IntegerTypeSuffix? ;

BooleanLiteral
    :   'true'
    |   'false'
    ;

fragment
HexDigit : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
IntegerTypeSuffix : ('l'|'L') ;

FloatingPointLiteral
:('0'..'9')+ '.' ('0'..'9')* Exponent? FloatTypeSuffix?
    |   '.' ('0'..'9')+ Exponent? FloatTypeSuffix?
    |   ('0'..'9')+ Exponent FloatTypeSuffix?
    |   ('0'..'9')+ FloatTypeSuffix
    ;

fragment
Exponent : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
FloatTypeSuffix : ('f'|'F'|'d'|'D') ;

CharacterLiteral
    :   '\'' ( EscapeSequence | ~('\''|'\\') )* '\''
    ;

StringLiteral
    :  '"' ( EscapeSequence | ~('\\'|'"') )* '"'
    ;


fragment
EscapeSequence
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UnicodeEscape
    |   OctalEscape
    ;

fragment
OctalEscape
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

    
Identifier 
    :   Letter (Letter|JavaIDDigit)*
    ;


fragment
Letter
    :  '\u0024' |
       '\u0041'..'\u005a' |
       '\u005f' |
       '\u0061'..'\u007a' |
       '\u00c0'..'\u00d6' |
       '\u00d8'..'\u00f6' |
       '\u00f8'..'\u00ff' |
       '\u0100'..'\u1fff' |
       '\u3040'..'\u318f' |
       '\u3300'..'\u337f' |
       '\u3400'..'\u3d2d' |
       '\u4e00'..'\u9fff' |
       '\uf900'..'\ufaff'
    ;

fragment
JavaIDDigit
    :  '\u0030'..'\u0039' |
       '\u0660'..'\u0669' |
       '\u06f0'..'\u06f9' |
       '\u0966'..'\u096f' |
       '\u09e6'..'\u09ef' |
       '\u0a66'..'\u0a6f' |
       '\u0ae6'..'\u0aef' |
       '\u0b66'..'\u0b6f' |
       '\u0be7'..'\u0bef' |
       '\u0c66'..'\u0c6f' |
       '\u0ce6'..'\u0cef' |
       '\u0d66'..'\u0d6f' |
       '\u0e50'..'\u0e59' |
       '\u0ed0'..'\u0ed9' |
       '\u1040'..'\u1049'
   ;

WS  :  (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;}
    ;

COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;



