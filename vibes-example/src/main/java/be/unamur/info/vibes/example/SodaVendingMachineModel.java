
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
        
        from(S[0]).action("pay").fexpr("!FreeDrinks").to(S[1]);
        from(S[0]).action("free").fexpr("FreeDrinks").to(S[2]);
        
        from(S[1]).action("change").to(S[2]);
        
        from(S[2]).action("cancel").fexpr("CancelPurchase").to(S[3]);
        
        from(S[3]).action("return").to(S[0]);
        from(S[3]).action("soda").fexpr("Soda").to(S[4]);
        from(S[3]).action("tea").fexpr("Tea").to(S[5]);
        
        from(S[4]).action("serveSoda").to(S[6]);
        
        from(S[5]).action("serveTea").to(S[6]);
        
        from(S[6]).action("take").fexpr("FreeDrinks").to(S[0]);
        from(S[6]).action("open").fexpr("!FreeDrinks").to(S[7]);
        
        from(S[7]).action("take").to(S[8]);
        
        from(S[8]).action("close").to(S[0]);
    }
    
}
