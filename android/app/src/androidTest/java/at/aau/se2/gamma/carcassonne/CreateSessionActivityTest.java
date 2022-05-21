package at.aau.se2.gamma.carcassonne;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.not;

import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import at.aau.se2.gamma.carcassonne.exceptions.NoServerInstanceException;
import at.aau.se2.gamma.carcassonne.network.ServerThread;
import at.aau.se2.gamma.carcassonne.utils.Logger;
import at.aau.se2.gamma.carcassonne.views.SelectNameActivity;
import at.aau.se2.gamma.core.ServerResponse;
import at.aau.se2.gamma.core.commands.BaseCommand;
import at.aau.se2.gamma.core.commands.DisconnectCommand;

public class CreateSessionActivityTest {

    @Rule
    public ActivityScenarioRule<SelectNameActivity> activityRule = new ActivityScenarioRule<>(SelectNameActivity.class);

    @Before
    public void setUp() throws Exception {
        /*
        //use this delay to start the server after starting the test if you get classNotFoundException
        Logger.debug("Server starten!");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        String etText = getTextOfEditText(onView(withId(R.id.pt_UserName)));

        //enter a Name if empty
        if(etText == "" || etText == null){
            onView(withId(R.id.pt_UserName)).perform(typeText("Test-User"), ViewActions.closeSoftKeyboard());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //navigate to MainActivity
        onView(withId(R.id.btn_NameSelectEnter)).perform(forceClick());

        //wait for server response
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        Logger.debug("test_END");

        try {
            if (ServerThread.instance == null) {
                throw new NoServerInstanceException();
            }
            ServerThread serverThread = ServerThread.instance;
            serverThread.sendCommand(new DisconnectCommand(null), new ServerThread.RequestResponseHandler() {

                @Override
                public void onResponse(ServerResponse response, Object payload, BaseCommand request) {
                    Logger.debug("test_END_onResponse");
                }

                @Override
                public void onFailure(ServerResponse response, Object payload, BaseCommand request) {
                    Logger.debug("test_END_onFailure");
                }
            });

        } catch (NoServerInstanceException e) {
            Logger.error(e.getMessage());
        }
    }

    @Test
    public void test_navigate_MainActivity() {
        //check MainActivity
        onView(withId(R.id.btn_navigate_create_session)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_navigate_join_session)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_BackToLobby)).check(matches(not(isDisplayed())));
        onView(withId(R.id.pb_menu)).check(matches(not(isDisplayed())));
        onView(withId(R.id.btn_gameplay_test)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_ui_elements)).check(matches(isDisplayed()));
    }

    @Test
    public void test_navigate_CreateSessionActivity() {
        //navigate to CreateSessionActivity
        onView(withId(R.id.btn_navigate_create_session)).perform(forceClick());

        //check CreateSessionActivity
        onView(withId(R.id.textView_instruction_sessionname)).check(matches(isDisplayed()));
        onView(withId(R.id.editText_sessionname)).check(matches(isDisplayed()));
        onView(withId(R.id.button_create_session)).check(matches(isDisplayed()));
        onView(withId(R.id.textView_error)).check(matches(not(isDisplayed())));
        onView(withId(R.id.progressBar_JoinSessionActivity)).check(matches(not(isDisplayed())));
    }

    @Test
    public void test_with_input() {
        onView(withId(R.id.btn_navigate_create_session)).perform(forceClick());

        //enter Sessionname
        onView(withId(R.id.editText_sessionname)).perform(typeText("Test-Session"), ViewActions.closeSoftKeyboard());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //click create session button
        onView(withId(R.id.button_create_session)).perform(forceClick());
        //onView(withId(R.id.textView_error)).check(matches(withText("Session created!")));

        //wait for server response
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //check if lobby is displayed
        onView(withId(R.id.tv_lobby_name)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_player_count)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_LeaveLobby)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_start_game)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_lobby)).check(matches(isDisplayed()));
    }

    @Test
    public void test_without_input() {
        onView(withId(R.id.btn_navigate_create_session)).perform(forceClick());

        //click create session button
        onView(withId(R.id.button_create_session)).perform(forceClick());

        //check if textView_error is displayed with correct text
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

    public static String getTextOfEditText(ViewInteraction vi) {
        try {
            final String[] stringHolder = {null};
            vi.perform(new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return isAssignableFrom(EditText.class);
                }

                @Override
                public String getDescription() {
                    return "get text";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    EditText editText = (EditText) view;
                    stringHolder[0] = editText.getText().toString();
                }
            });

            return stringHolder[0];

        } catch (Exception e) {
            return e.toString();
        }
    }
}