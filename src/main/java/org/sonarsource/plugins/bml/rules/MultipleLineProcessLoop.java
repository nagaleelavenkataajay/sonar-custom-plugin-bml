package org.sonarsource.plugins.bml.rules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

@Rule(key = MultipleLineProcessLoop.RULE_KEY, name = "Multiple Line Processing Loop ", description = "File shall not have multiple loops")
public class MultipleLineProcessLoop implements FlagLineRule {
  public static final String RULE_KEY = "MultipleLineProcessingLoop";
//@Rule(key = FlagLine3Rule.RULE_KEY, name = "Line Process Rule", description = "Checks for excessive use of 'line_process' in the file.")
//public class FlagLine3Rule implements FlagLineRule {
//    public static final String RULE_KEY = "ExampleRule3";

//  @Override
//  public void execute(SensorContext sensorContext, InputFile file, RuleKey ruleKey) {
//	NewIssue newIssue = sensorContext.newIssue();
//    newIssue
//      .forRule(ruleKey)
//      .at(newIssue.newLocation()
//        .on(file)
//        .at(file.selectLine(1)))
//      .save();
//  }
  
   @Override
    public void execute(SensorContext sensorContext, InputFile file, RuleKey ruleKey) {
        try (BufferedReader br = new BufferedReader(new FileReader(file.toString()))) {
            int lineNumber = 0;
            int lineProcessCount = 0;
 
            String line;
            while ((line = br.readLine()) != null) {
                lineNumber++;
 
                // Check if the line contains 'line_process'
                if (line.contains("line_process")) {
                    lineProcessCount++;
                }
            }
 
            float calc = (float) lineNumber;
            float threshold = calc / 500;
            double roundedThreshold = Math.ceil(threshold);
 
            // Check if the number of 'line_process' occurrences exceeds the threshold
            if (lineProcessCount > roundedThreshold) {
                NewIssue newIssue = sensorContext.newIssue();
                newIssue.forRule(ruleKey)
                        .at(newIssue.newLocation().on(file))
                        .save();
                System.out.println("Excessive use of 'line_process' detected in the file: " + file.toString());
            }
 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
