# Android Gummy

Gummy lib for Android allows you to easily add a gummy behaviour on any view. The gummy behaviour gives you a chance to have different actions on pull event and click event. The pull animation is fully managed and highly customisable.

## Requirements

* Android SDK 15+

# Usage

```java
        PullClickListener pullClickListener = new PullClickListener(mContext) {
            @Override
            protected void onPull() {
                Toast.makeText(mContext, "Pull! 🐓", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onClick() {
                Toast.makeText(mContext, "Click", Toast.LENGTH_SHORT).show();
            }
        };

        pullClickListener
                // Optional animation interpolator
                .setInterpolator(new OvershootInterpolator(4f))
                // Optional amplitude in dp
                .setAmplitude(90)
                // Optional resistance, 0f means no resistance, 1f means you shall never move the view
                .setResistance(0.90f)
                // Optional minimum move in dp to trigger a pull event
                .setTriggerAt(85)
                // Optional animation duration
                .setAnimationDuration(150);

        fab.setOnTouchListener(pullClickListener);
```

## Result
___
//![pullclick](https://bitbucket.org/repo/rpKXzRX/images/2708637862-pullclick.gif)
___

**Have fun!**