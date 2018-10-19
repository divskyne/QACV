package LogInTests;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import com.qa.cv.Constants;

@RunWith(Cucumber.class)
@CucumberOptions(features = (Constants.FEATUREFILE))
public class LogInTestRunner {

}
