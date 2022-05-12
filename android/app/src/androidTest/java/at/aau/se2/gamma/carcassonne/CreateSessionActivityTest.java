package at.aau.se2.gamma.carcassonne;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
/*
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
*/
import at.aau.se2.gamma.carcassonne.R;
import at.aau.se2.gamma.carcassonne.views.CreateSessionActivity;


public class CreateSessionActivityTest {
/*
    @Mock
    private BaseActivity baseActivityMock;

    @Mock
    private CreateGameCommand createGameCommandMock;

    @Mock
    private ServerThread serverThreadMock;

    @InjectMocks
    CreateSessionActivity createSessionActivityMock;
*/
    @Rule
    public ActivityScenarioRule<CreateSessionActivity> activityRule = new ActivityScenarioRule<>(CreateSessionActivity.class);

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void test_with_input() {     //fix this
        onView(withId(R.id.editText_sessionname)).perform(typeText("Test-Session"), ViewActions.closeSoftKeyboard());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.button_create_session)).perform(forceClick());
        onView(withId(R.id.textView_error)).check(matches(withText("Session created!")));
    }

    @Test
    public void test_without_input() {
        onView(withId(R.id.editText_sessionname)).perform(ViewActions.closeSoftKeyboard());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.button_create_session)).perform(forceClick());
        onView(withId(R.id.textView_error)).check(matches(withText("Choose a name for your game!")));
    }

    public static ViewAction forceClick() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isClickable(), isEnabled(), isDisplayed());
            }

            @Override
            public String getDescription() {
                return "force click";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.performClick(); // perform click without checking view coordinates.
                uiController.loopMainThreadUntilIdle();
            }
        };
    }
}