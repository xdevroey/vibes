grammar Fexpression;

expression: 
	  val=TRUE                                      # constantTrue
        | val=FALSE                                     # constantFalse
        | name=FEATURE                                  # featureName
        | '(' exp=expression ')'                        # parenthesis
        | NOT op=expression                             # notExpr
        | opLeft=expression operator=(AND|OR) opRight=expression # andOrExpr        
;

AND : '&&';
OR : '||';
NOT : '!';

TRUE : 'true';
FALSE : 'false';

FEATURE: 
	LETTER (LETTER | DIGIT)* 
;

// Whitespaces -> ignored

NL: 
	'\n' | '\r'
;
 
WS: 
	(' ' | '\t' | NL) { skip(); }
;

fragment LETTER
	:	'A'..'Z' | 'a'..'z' | '_';

fragment DIGIT
	:	'0'..'9';