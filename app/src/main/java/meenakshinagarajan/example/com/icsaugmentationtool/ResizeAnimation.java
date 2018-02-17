package meenakshinagarajan.example.com.icsaugmentationtool;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by meenakshinagarajan on 10/17/17.
 */

public class ResizeAnimation extends Animation {
    final int targetWidth,targetHeight;
    View view;
    int startWidth,startHeight;

    public ResizeAnimation(View view, int targetWidth, int startWidth,int targetHeight,int startHeight) {
        this.view = view;
        this.targetWidth = targetWidth;
        this.startWidth = startWidth;
        this.targetHeight = targetHeight;
        this.startHeight = targetHeight;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newWidth = (int) (startWidth + (targetWidth-startWidth) * interpolatedTime);
        int newHeight = (int) (startHeight + (targetHeight-startHeight) * interpolatedTime);
        //to support decent animation, change new heigt as Nico S. recommended in comments
        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().width = newWidth;
        view.getLayoutParams().height=newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
