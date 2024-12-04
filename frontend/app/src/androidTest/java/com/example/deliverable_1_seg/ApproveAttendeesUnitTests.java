package com.example.deliverable_1_seg;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.deliverable_1_seg.user_actions.AttendeeLoginActivity;
import com.example.deliverable_1_seg.user_actions.OrganizerLoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ApproveAttendeesUnitTests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp() {
        Intents.init();  // Initialize Intents for intent verification
    }

    @After
    public void tearDown() {
        Intents.release();  // Release Intents after the test is complete
    }

    @Test
    public void testAttendeeButtonLaunchesAttendeeLoginActivity() {
        onView(withId(R.id.Attendee_button)).perform(click());
        intended(hasComponent(AttendeeLoginActivity.class.getName()));
    }

    @Test
    public void testAttendeeButtonLaunchesAttendeeLoginActivityFail() {
        onView(withId(R.id.Attendee_button)).perform(click());
        intended(hasComponent(OrganizerLoginActivity.class.getName()));
    }
}
