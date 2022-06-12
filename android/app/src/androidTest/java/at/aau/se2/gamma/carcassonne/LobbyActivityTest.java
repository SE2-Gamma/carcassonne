package at.aau.se2.gamma.carcassonne;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static at.aau.se2.gamma.carcassonne.CreateSessionActivityTest.forceClick;
import static at.aau.se2.gamma.carcassonne.CreateSessionActivityTest.getTextOfEditText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

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

public class LobbyActivityTest {

    @Rule
    public ActivityScenarioRule<SelectNameActivity> activityRule = new ActivityScenarioRule<>(SelectNameActivity.class);

    @Before
    public void setUp() {
        String userName = getTextOfEditText(onView(withId(R.id.pt_UserName)));

        if(userName == "" || userName == null) {
            onView(withId(R.id.pt_UserName)).perform(typeText("Test-User"), ViewActions.closeSoftKeyboard());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        onView(withId(R.id.btn_NameSelectEnter)).perform(forceClick());

        //wait for server response
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //navigate to create Session Screen
        onView(withId(R.id.btn_navigate_create_session)).perform(forceClick());

        //enter session name
        onView(withId(R.id.editText_sessionname)).perform(typeText("Test-Session"), ViewActions.closeSoftKeyboard());


        //create Session and join Lobby
        onView(withId(R.id.button_create_session)).perform(forceClick());

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
    public void test_LobbyActivity() {
        onView(withId(R.id.tv_lobby_name)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_player_count)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_LeaveLobby)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_ready)).check(matches(isDisplayed()));
    }
}
