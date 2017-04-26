
package be.unamur.info.vibes.example;

import be.unamur.transitionsystem.dsl.FeaturedTransitionSystemDefinition;

/**
 * Model of the Soda Vending Machine product line
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class SodaVendingMachineModel extends FeaturedTransitionSystemDefinition{
    
    private static final String[] S = new String[]{"s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9"};

    @Override
    protected void define() {
        initial(S[0]);
        
        from(S[0]).action("pay").fexpr("!f").to(S[1]);
        from(S[0]).action("free").fexpr("f").to(S[2]);
        
        from(S[1]).action("change").fexpr("!f").to(S[2]);
        
        from(S[2]).action("cancel").fexpr("c").to(S[3]);
        
        from(S[3]).action("return").fexpr("c").to(S[0]);
        from(S[3]).action("soda").fexpr("s").to(S[4]);
        from(S[3]).action("tea").fexpr("t").to(S[5]);
        
        from(S[4]).action("serveSoda").fexpr("s").to(S[6]);
        
        from(S[5]).action("serveTea").fexpr("t").to(S[6]);
        
        from(S[6]).action("take").fexpr("f").to(S[0]);
        from(S[6]).action("open").fexpr("!f").to(S[7]);
        
        from(S[7]).action("take").fexpr("!f").to(S[8]);
        
        from(S[8]).action("close").fexpr("!f").to(S[0]);
    }
    
}
