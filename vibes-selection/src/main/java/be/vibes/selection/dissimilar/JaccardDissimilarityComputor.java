package be.vibes.selection.dissimilar;

/*-
 * #%L
 * VIBeS: test case selection
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Set;

import com.google.common.collect.Sets;

import static com.google.common.base.Preconditions.*;

public class JaccardDissimilarityComputor<T extends Set> implements
        SetBasedDissimilarityComputor<T> {
    
    private double w;
    
    protected JaccardDissimilarityComputor(double w){
        this.w = w;
    }
    
    public JaccardDissimilarityComputor(){
        this(1);
    }

    @Override
    public double dissimilarity(T s1, T s2) {
        return 1 - getDistance(s1, s2);
    }
    
    public double getDistance(T s1, T s2) {
        if(s1.size() == 0 && s2.size() == 0){
            return 1.0;
        } 
        double intersect = (double) Sets.intersection(s1, s2).size();
        double union = (double) Sets.union(s1, s2).size();
        checkState(intersect <= union, "Union has less products than intersection!");
        return (intersect / (intersect + w * (union - intersect) ));
    }

}
