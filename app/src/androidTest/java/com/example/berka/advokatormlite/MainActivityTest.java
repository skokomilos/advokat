package com.example.berka.advokatormlite;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.berka.advokatormlite.activities.main.MainActivity;
import com.example.berka.advokatormlite.model.Postupak;

import static android.support.test.espresso.Espresso.onView;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by berka on 02-Feb-18.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Test
    public void clickedAddCardView() throws SQLException {

        ArrayList<Postupak> postupci = (ArrayList<Postupak>) mActivityTestRule.getActivity().getDatabaseHelper().getPostupakDao().queryForAll();

        onView(withId(R.id.cv_add)).perform(click());
        onView(withText("Izaberi postupak")).check(matches(isDisplayed()));
        onView(withText("Ok potvrdjujem")).check(matches(isClickable()));
        onView(withId(R.id.broj_stranaka)).perform(typeText("2"));
        onView(withText("Ok potvrdjujem")).perform(click());
    }
}
