package be.unamur.transitionsystem.test.mutation.equivalence.main;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import static be.unamur.transitionsystem.dsl.TransitionSystemXmlLoaders.loadLabelledTransitionSystem;

/**
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 */
public class LtsFileIterator implements Iterator<LabelledTransitionSystem> {

    private Iterator<File> inputFilesIterator;
    private String lastLtsFileName;
    
    public LtsFileIterator(String fileName){
        this(new File(fileName));
    }

    public LtsFileIterator(File input) {
        List<File> inputLts = Lists.newLinkedList();
        if (input.isDirectory()) {
            for (File f : input.listFiles()) {
                if (f.getName().endsWith(".lts") || f.getName().endsWith(".ts")) {
                    inputLts.add(f);
                }
            }
        } else {
            inputLts.add(input);
        }
        inputFilesIterator = inputLts.iterator();
    }

    @Override
    public boolean hasNext() {
        return inputFilesIterator.hasNext();
    }

    @Override
    public LabelledTransitionSystem next() {
        File next = inputFilesIterator.next();
        lastLtsFileName = next.getName();
        LabelledTransitionSystem lts = loadLabelledTransitionSystem(next);
        return lts;
    }

    public String getLastLtsFileName() {
        return lastLtsFileName;
    }
    
}
