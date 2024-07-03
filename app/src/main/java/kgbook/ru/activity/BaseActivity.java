package kgbook.ru.activity;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;


import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

import kgbook.ru.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();//            case R.string.start_test_again:
//                return false;
        if (itemId == R.id.action_about) {//Module_U.aboutDialog(this);
//                startActivity(new Intent(getApplicationContext(), ActivityAbout.class));
//                overridePendingTransition(R.anim.open_next, R.anim.close_main);
            return true;
        } else if (itemId == R.id.action_privacy_policy) {
            Launcher.openBrowser(this, "https://google.com");
            return true;
        } else if (itemId == R.id.action_rate_app) {
            Launcher.rateUs(this);
            return true;
        } else if (itemId == R.id.action_share_app) {
            Module_U.shareThisApp(this);
            return true;

//            case R.id.action_discover_more_app:
//                Module_U.moreApp(this);
//                return true;

//            case R.id.action_exit:
//                this.finish();
//                return true;
        } else if (itemId == R.id.action_feedback) {
            Module_U.feedback(this);
            return true;
        }
        return super.onOptionsItemSelected(item);


        //action_how_to_use_app
        //action_support_developer

        //return super.onOptionsItemSelected(item);
    }
}
