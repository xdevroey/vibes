package be.unamur.transitionsystem.stat;

/*
 * #%L vibes-core %% Copyright (C) 2014 PReCISE, University of Namur %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. #L%
 */
import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.usagemodel.UsageModel;

/**
 * {@link ModelStatistics} factory class.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class ModelStatisticsBuilder {

    public static final ModelStatisticsBuilder INSTANCE = new ModelStatisticsBuilder();

    private ModelStatisticsBuilder() {
    }

    public ModelStatistics build(LabelledTransitionSystem ts) {
        return new LabelledTransitionSystemStatistics(ts);
    }

    public ModelStatistics build(FeaturedTransitionSystem fts) {
        return new FeaturedTransitionSystemStatistics(fts);
    }

    public ModelStatistics build(UsageModel usageModel) {
        return new UsageModelStatistics(usageModel);
    }

}
