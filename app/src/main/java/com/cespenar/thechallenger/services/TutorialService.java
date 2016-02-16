package com.cespenar.thechallenger.services;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.cespenar.thechallenger.CreateChallengeActivity;
import com.cespenar.thechallenger.MainActivity;
import com.cespenar.thechallenger.R;

import tourguide.tourguide.Overlay;
import tourguide.tourguide.Pointer;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;

/**
 * Created by Jasbuber on 06/11/2015.
 */
public class TutorialService {

    private static TutorialService service;

    private static TourGuide tourGuide;

    private TutorialService() {
    }

    public static TutorialService getService() {
        if (service == null) {
            service = new TutorialService();
        }

        return service;
    }

    public static boolean isTutorialActive() {
        return !UserService.getCurrentUser().isTutorialCompleted();
    }

    public void startTutorial(final MainActivity activity) {

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        showStep(activity, R.string.tutorial_title1, R.string.tutorial_message1,
                R.id.create_challenge_block, true);

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_start_tutorial);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Button dismissAction = (Button) dialog.findViewById(R.id.tutorial_dialog_cancel_action);
        Button startAction = (Button) dialog.findViewById(R.id.tutorial_dialog_start_action);

        dismissAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                tourGuide.cleanUp();
                UserService.getCurrentUser().completeTutorial();
                UserService.getService().completeTutorial(activity);
            }
        });

        startAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void showStep(Activity activity, int titleId, int messageId, int elementId,
                                boolean isPointer) {
        if (tourGuide != null) {
            tourGuide.cleanUp();
        }

        tourGuide = TourGuide.init(activity).with(TourGuide.Technique.Click);

        if (isPointer) {
            tourGuide.setPointer(new Pointer());
        }
        tourGuide.setToolTip(new ToolTip()
                .setTitle(activity.getString(titleId))
                .setDescription(activity.getString(messageId)))
                .setOverlay(new Overlay())
                .playOn(activity.findViewById(elementId));
    }

    public static void cleanUp(final MainActivity activity) {
        tourGuide.cleanUp();
        UserService.getCurrentUser().completeTutorial();

        UserService.getService().completeTutorial(activity);

        activity.findViewById(R.id.browse_challenges_block).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onClickBrowseChallenges(v);
            }
        });

        activity.findViewById(R.id.rankings_block).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onClickRankings(v);
            }
        });

        activity.findViewById(R.id.my_challenges_block).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onClickMyChallenges(v);
            }
        });
    }

    public static TourGuide getTourGuide() {
        return tourGuide;
    }

    public void handleTutorial(Activity activity) {

        if(activity instanceof MainActivity){
            handleMainActivity((MainActivity) activity);
        }else if(activity instanceof CreateChallengeActivity){
            handleCreateChallenge((CreateChallengeActivity) activity);
        }
    }

    private static void handleCreateChallenge(final CreateChallengeActivity activity) {

        if (!isTutorialActive()) {
            return;
        }

        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TutorialService.showStep(activity, R.string.tutorial_title1, R.string.tutorial_message2, R.id.first_step, false);

        activity.findViewById(R.id.show_second_step_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onClickStepOne(v);
                TutorialService.showStep(activity, R.string.tutorial_title1, R.string.tutorial_message3, R.id.second_step, false);

                activity.findViewById(R.id.show_third_step_action).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onClickStepTwo(v);

                        TourGuide guide = TutorialService.getTourGuide();
                        guide.cleanUp();
                        guide.setToolTip(new ToolTip().setGravity(Gravity.TOP)
                                .setTitle(activity.getString(R.string.tutorial_title1))
                                .setDescription(activity.getString(R.string.tutorial_message4)))
                                .setOverlay(new Overlay())
                                .playOn(activity.findViewById(R.id.third_step));

                        Button continueAction = (Button) activity.findViewById(R.id.create_challenge_continue_tutorial);
                        activity.findViewById(R.id.create_challenge_submit).setVisibility(View.GONE);
                        continueAction.setVisibility(View.VISIBLE);
                        continueAction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, MainActivity.class);
                                intent.putExtra("tutorialStep", 1);
                                activity.startActivity(intent);
                            }
                        });
                    }
                });
            }
        });
    }

    private static void handleMainActivity(final MainActivity activity) {

        if (activity.getIntent().getIntExtra("tutorialStep", -1) == -1) {
            return;
        }
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TutorialService.showStep(activity, R.string.tutorial_title2, R.string.tutorial_message5,
                R.id.browse_challenges_block, true);

        activity.findViewById(R.id.browse_challenges_block).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TutorialService.showStep(activity, R.string.tutorial_title3, R.string.tutorial_message6,
                        R.id.rankings_block, true);

                activity.findViewById(R.id.rankings_block).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TutorialService.showStep(activity, R.string.tutorial_title4, R.string.tutorial_message7,
                                R.id.my_challenges_block, true);

                        activity.findViewById(R.id.my_challenges_block).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Button finishButton = (Button) activity.findViewById(R.id.tutorial_complete_action);
                                finishButton.setVisibility(View.VISIBLE);

                                TourGuide guide = TutorialService.getTourGuide();
                                guide.cleanUp();
                                guide.setToolTip(new ToolTip().setGravity(Gravity.TOP | Gravity.LEFT)
                                        .setTitle(activity.getString(R.string.tutorial_title5))
                                        .setDescription(activity.getString(R.string.tutorial_message8)))
                                        .setOverlay(new Overlay())
                                        .setPointer(new Pointer())
                                        .playOn(activity.findViewById(R.id.tutorial_complete_action));

                                finishButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        v.setVisibility(View.GONE);
                                        TutorialService.cleanUp(activity);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

}
