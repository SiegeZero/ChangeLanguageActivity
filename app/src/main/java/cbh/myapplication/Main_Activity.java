package cbh.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by gsb on 5/6/17.
 */

public class Main_Activity extends Activity {
    static String[] languages = new String[]{"正在切换为\n简体中文","正在切換為\n繁体中文","Setting to\nEnglish"};
    static String[] title = new String[]{"语言切换","語言切換","Language"};
    Context context;
    int last_selected_position = 0;
    int saved_selected_position = 0;
    AlertDialog alertDialog;
    ImageButton tosave_btn;
    Button back_btn;
    Window loading_window;
    Handler handler;
    ImageView loading_ImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.main_layout);
        context = this;
        back_btn = (Button) findViewById( R.id.id_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_Activity.this.finish();
            }
        });
        tosave_btn = (ImageButton) findViewById( R.id.id_toSave_btn);
        tosave_btn.setClickable( false);
        tosave_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean set_up_succeed = false;
                tosave_btn.setBackgroundResource( R.drawable.saved_btn_style);
                saved_selected_position = last_selected_position;

                alertDialog = new AlertDialog
                    .Builder( context)
                    .create();
                if( !alertDialog.isShowing())
                    alertDialog.show();
                loading_window = alertDialog.getWindow();
                loading_window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
                loading_window.setContentView( R.layout.changing_language_alertdialog_layout);
                TextView tv = (TextView) loading_window.findViewById(R.id.id_changing_language);
                tv.setText( languages[last_selected_position]);
                loading_ImageView = (ImageView) loading_window.findViewById(R.id.id_loading_icon);
                /* Another way to set up an animation without xml file

                   Animation rotate_anim = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                   rotate_anim.setRepeatCount( -1);
                   rotate_anim.setDuration( 500);
                   rotate_anim.setInterpolator( new AccelerateDecelerateInterpolator());
                   rotate_anim.setStartOffset( -1);
                   rotate_anim.setAnimationListener(new Animation.AnimationListener() {
                       @Override
                       public void onAnimationStart(Animation animation) {

                       }

                       @Override                        public void onAnimationEnd(Animation animation) {
                           if( alertDialog.isShowing()){
                               loading_ImageView.startAnimation( animation);
                           }
                       }

                       @Override
                       public void onAnimationRepeat(Animation animation) {

                       }
                   });
                 */
                Animation rotate_anim = AnimationUtils
                        .loadAnimation( context, R.anim.loading_id_rotate);
                //load the pre-set animation
                rotate_anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if( alertDialog.isShowing()){
                            loading_ImageView.startAnimation( animation);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                if( rotate_anim != null)
                    loading_ImageView.startAnimation( rotate_anim);
                handler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        alertDialog.dismiss();
                        return false;
                    }
                });
                Runnable setGlobalLanguage = new Runnable() {
                    @Override
                    public void run() {
                        //start to set global language
                        changeLanguage();
                        //end the language setting up
                        handler.sendMessage( new Message());
                    }
                };
                handler.postDelayed( setGlobalLanguage, 1000);
                tosave_btn.setClickable( false);
            }

            private void changeLanguage() {
                ((TextView) findViewById( R.id.id_title)).setText( title[saved_selected_position]);
                //缺少保存按钮的更换图片导致代码省缺
            }
        });
    }
    public void selectScn( View v){
        if( last_selected_position != 0) {
            findViewById( R.id.id_scn_selected_state).setVisibility( View.VISIBLE);
            unSelectIcon( 0);
            last_selected_position = 0;

        }
    }

    public void selectTcn( View v){
        if( last_selected_position != 1){
            findViewById( R.id.id_tcn_selected_state).setVisibility( View.VISIBLE);
            unSelectIcon( 1);
            last_selected_position = 1;
        }
    }

    public void selectEn( View v){
        if (last_selected_position != 2) {
            findViewById( R.id.id_en_selected_state).setVisibility( View.VISIBLE);
            unSelectIcon( 2);
            last_selected_position = 2;
        }
    }
    private void unSelectIcon( int current_position) {
        boolean SET_NO_CHANGE = saved_selected_position != current_position;
        tosave_btn.setBackgroundResource(
                SET_NO_CHANGE?
                        R.drawable.tosave_btn_style:R.drawable.saved_btn_style
        );
        tosave_btn.setClickable( SET_NO_CHANGE);

        switch( last_selected_position){
            case 0:{
                findViewById( R.id.id_scn_selected_state).setVisibility( View.GONE);
                break;
            }
            case 1:{
                findViewById( R.id.id_tcn_selected_state).setVisibility(View.GONE);
                break;
            }
            case 2:{
                findViewById( R.id.id_en_selected_state).setVisibility(View.GONE);
                break;
            }
            default:
                Log.e("unhandled case", "unSelectIcon: case = "+last_selected_position);
        }
    }

}
