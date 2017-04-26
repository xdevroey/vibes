package be.unamur.transitionsystem.dsl.test.mutation;

/*
 * #%L
 * vibes-dsl
 * %%
 * Copyright (C) 2014 PReCISE, University of Namur
 * %%
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import be.unamur.transitionsystem.LabelledTransitionSystem;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.TransitionSystem;
import be.unamur.transitionsystem.dsl.exception.MutantExecutionException;
import be.unamur.transitionsystem.fts.FeaturedTransitionSystem;
import be.unamur.transitionsystem.test.mutation.FeaturedMutantsModel;
import be.unamur.transitionsystem.test.mutation.MutationConfiguration;
import be.unamur.transitionsystem.test.mutation.MutationOperator;
import be.unamur.transitionsystem.test.mutation.exception.CounterExampleFoundException;
import be.unamur.transitionsystem.test.mutation.exception.MutationConfigurationException;
import be.unamur.transitionsystem.test.mutation.exception.MutationException;
import be.unamur.transitionsystem.transformation.xml.ElementPrinter;
import be.unamur.transitionsystem.transformation.xml.FtsPrinter;
import be.unamur.transitionsystem.transformation.xml.LtsPrinter;
import be.unamur.transitionsystem.transformation.xml.UsageModelPrinter;
import be.unamur.transitionsystem.transformation.xml.XmlPrinter;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import static com.google.common.base.Preconditions.*;

public class ConfiguredMutagen {

    private static final Logger LOG = LoggerFactory
            .getLogger(ConfiguredMutagen.class);

    public static final String VALID_PROD_FEXPR = "prod";

    private File configurationFile = null;
    private File outputDir = null;
    private File ftsMutant = null;
    private File tvlMutant = null;

    /**
     * Creates a new configuration to perform mutation using the provided
     * configuration file.
     *
     * @param file
     * @return
     */
    public static ConfiguredMutagen configure(String file) {
        return configure(new File(file));
    }

    /**
     * Creates a new configuration to perform mutation using the provided
     * configuration file.
     *
     * @param file
     * @return
     */
    public static ConfiguredMutagen configure(File file) {
        return new ConfiguredMutagen(file);
    }

    ConfiguredMutagen(File configurationFile) {
        this.configurationFile = configurationFile;
    }

    /**
     * Specifies the output directory for the enumerative generation of mutants.
     * Set the dir parameter to null to disable enumerative mutant generation.
     * The given directory will be emptied before mutants generation.
     *
     * @param dir
     * @return
     */
    public ConfiguredMutagen outputDir(File dir) {
        this.outputDir = dir;
        return this;
    }

    /**
     * Specifies the output directory for the enumerative generation of mutants.
     * Set the dir parameter to null to disable enumerative mutant generation.
     * The given directory will be emptied before mutants generation.
     *
     * @param dir
     * @return
     */
    public ConfiguredMutagen outputDir(String dir) {
        return outputDir(new File(dir));
    }

    /**
     * Specifies the name of the FMM's FTS. Set the file parameter to null to
     * disable FMM's FTS generation.
     *
     * @param file
     * @return
     */
    public ConfiguredMutagen ftsMutant(File file) {
        this.ftsMutant = file;
        return this;
    }

    /**
     * Specifies the name of the FMM's FTS. Set the file parameter to null to
     * disable FMM's FTS generation.
     *
     * @param file
     * @return
     */
    public ConfiguredMutagen ftsMutant(String file) {
        return ftsMutant(new File(file));
    }

    /**
     * Specifies the name of the FMM's FD. Set the file parameter to null to
     * disable FMM's FD generation.
     *
     * @param file
     * @return
     */
    public ConfiguredMutagen tvlMutant(File file) {
        this.tvlMutant = file;
        return this;
    }

    /**
     * Specifies the name of the FMM's FD. Set the file parameter to null to
     * disable FMM's FD generation.
     *
     * @param file
     * @return
     */
    public ConfiguredMutagen tvlMutant(String file) {
        return tvlMutant(new File(file));
    }

    /**
     * Launch the mutation of the given FTS using the current configuration.
     *
     * @param fts
     */
    public FeaturedMutantsModel mutate(FeaturedTransitionSystem fts) {
        return mutate(fts, new FtsPrinter(), ".fts");
    }

    /**
     * Launch the mutation of the given LTS using the current configuration.
     *
     * @param lts
     */
    public FeaturedMutantsModel mutate(LabelledTransitionSystem lts) {
        return mutate(lts, new LtsPrinter(), ".ts");
    }

    /**
     * Launch the mutation of the given Usage Model using the current
     * configuration.
     *
     * @param um
     */
    public FeaturedMutantsModel mutate(UsageModel um) {
        return mutate(um, new UsageModelPrinter(), ".usagemodel");
    }

    private FeaturedMutantsModel mutate(TransitionSystem ts, ElementPrinter elementPrinter,
            String extension) {
        checkNotNull(this.configurationFile, "Configuration file is null!");
        checkNotNull(ts, "Argument 'ts' is null!");
        checkNotNull(elementPrinter, "Argument 'elementPrinter' is null!");
        //Clean outputs
        try {
            File mappingFile = this.tvlMutant != null ? new File(
                    this.tvlMutant.getAbsolutePath() + ".mapping.csv") : null;
            cleanDir(this.outputDir);
            cleanFile(this.ftsMutant);
            cleanFile(this.tvlMutant);
            cleanFile(mappingFile);
            // Load Configuration file
            MutationConfiguration config = new MutationConfiguration(
                    this.configurationFile);
            List<MutationOperator> operators = config.getOperators(ts);
            // Process mutants 
            TransitionSystem mutant;
            FileOutputStream out;
            XmlPrinter printer = new XmlPrinter(System.out, elementPrinter);
            Map<String, Map<String, String>> keysByOperator = Maps.newHashMap();
            Map<String, String> mutationKeys;
            FeaturedMutantsModel fmm = new FeaturedMutantsModel(ts);
            List<List<String>> mutantsMapping = Lists.newArrayList();
            mutantsMapping.add(Lists.newArrayList("symbol", "uniquekey", "featureid",
                    "file", "modifiedstates"));
            List<String> mapping;
            String fileName;
            int max;
            for (MutationOperator op : operators) {
                mutationKeys = Maps.newHashMap();
                keysByOperator.put(op.getSymbol(), mutationKeys);
                max = config.getMutationSize(op.getClass());
                for (int i = 0; i < max; i++) {
                    LOG.debug("Mutant {} {}", op.getSymbol(), i);
                    op.apply();
                    if (!mutationKeys.containsKey(op.getMutationUniqueKey())) {
                        mapping = Lists.newArrayList();
                        mutationKeys.put(op.getMutationUniqueKey(), op.getFeatureId());
                        // Output mutant
                        fileName = op.getFeatureId() + extension;
                        if (this.outputDir != null) {
                            mutant = op.result();
                            fileName = this.outputDir.getAbsolutePath()
                                    + File.separatorChar + fileName;
                            out = new FileOutputStream(fileName);
                            printer.setOutput(out);
                            printer.print(mutant);
                            out.flush();
                            out.close();
                        }
                        // Get impacted states
                        StringBuilder impactedStates = new StringBuilder();
                        Iterator<State> itStates = op.getModifiedStates().iterator();
                        while (itStates.hasNext()) {
                            State s = itStates.next();
                            impactedStates.append(s.getName());
                            if (itStates.hasNext()) {
                                impactedStates.append('/');
                            }
                        }
                        // Transpose to FTS
                        try {
                            fmm.mutate(op);
                            mapping.add(op.getSymbol());
                            mapping.add(op.getMutationUniqueKey());
                            mapping.add(op.getFeatureId());
                            mapping.add(fileName);
                            mapping.add(impactedStates.toString());
                            mutantsMapping.add(mapping);
                            LOG.debug("Mutant {} {} DONE", op.getSymbol(), i);
                        } catch (CounterExampleFoundException e) {
                            LOG.debug("Equivalent mutant generated!");
                            i--;
                            new File(fileName).delete();
                        }
                    } else {
                        LOG.debug("Double mutant {} {}", op.getSymbol(), i);
                    }
                }
            }

            // Print TVL file + mapping
            if (this.tvlMutant != null) {
                // Print mapping
                PrintStream csvOut = new PrintStream(mappingFile);
                for (List<String> lst : mutantsMapping) {
                    Iterator<String> it = lst.iterator();
                    while (it.hasNext()) {
                        String s = it.next();
                        csvOut.print(s);
                        if (it.hasNext()) {
                            csvOut.print(';');
                        }
                    }
                    csvOut.println();
                }
                csvOut.flush();
                csvOut.close();
                // Print TVL
                out = new FileOutputStream(this.tvlMutant);
                fmm.printTvl(new PrintStream(out));
                out.flush();
                out.close();
            }

            // print FTS
            if (this.ftsMutant != null) {
                out = new FileOutputStream(this.ftsMutant);
                printer = new XmlPrinter(out, new FtsPrinter());
                printer.print(fmm.getFts());
                out.flush();
                out.close();
            }

            return fmm;
        } catch (IOException e) {
            throw new MutantExecutionException("Error while cleaning output files!", e);
        } catch (ConfigurationException e) {
            throw new MutantExecutionException("Error while loading configuration!", e);
        } catch (MutationConfigurationException e) {
            throw new MutantExecutionException("Error while loading configuration!", e);
        } catch (MutationException e) {
            throw new MutantExecutionException("Error during mutation process!", e);
        } catch (XMLStreamException e) {
            throw new MutantExecutionException("Error while priting XML!", e);
        }
    }

    private void cleanDir(File dir) {
        if (dir != null) {
            if (dir.exists() && dir.isDirectory()) {
                for (File f : dir.listFiles()) {
                    f.delete();
                }
            } else {
                dir.mkdir();
            }
        }
    }

    private void cleanFile(File f) throws IOException {
        if (f == null) {
            return;
        } else if (f.exists() && f.isFile()) {
            f.delete();
        }
        f.createNewFile();
    }

    private static void printTvl(PrintStream out,
            Map<String, Map<String, String>> keysByOperator) {

        out.println("/*");
        out.println(" * Generated on " + new GregorianCalendar().getTime());
        out.println(" */");
        out.println();
        out.println("root Mutants {");
        out.println("    group allof{");
        // Print operators fetaures
        Iterator<String> it = keysByOperator.keySet().iterator();
        while (it.hasNext()) {
            out.print("        opt " + it.next());
            if (it.hasNext()) {
                out.println(",");
            }
        }
        out.println();
        out.println("    }");
        out.println("}");
        out.println();
        // Print mutants features
        Iterator<Entry<String, String>> itEntry;
        for (Entry<String, Map<String, String>> mutants : keysByOperator.entrySet()) {
            out.println(mutants.getKey() + "{");
            out.println("    group allof{");
            // Print operators fetaures
            itEntry = mutants.getValue().entrySet().iterator();
            Entry<String, String> val;
            while (itEntry.hasNext()) {
                val = itEntry.next();
                out.println("      /* " + val.getKey() + " */");
                out.print("        opt " + val.getValue());
                if (itEntry.hasNext()) {
                    out.println(",");
                } else {
                    out.println();
                }
            }
            out.println("    }");
            out.println("}");
            out.println();
        }
    }

}
