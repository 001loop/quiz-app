package com.loop.quizapp;

    import android.app.Fragment;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.LinearLayout;

    public class WelcomeFragment extends Fragment {

        ButtonView btnNo;
        ButtonView btnYes;

        public WelcomeFragment() {
            // Required empty public constructor
        }

        @SuppressWarnings("deprecation")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_welcome, container, false);
            LinearLayout btnContainer = view.findViewById(R.id.btn_container);
            LinearLayout.LayoutParams btnContainerParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            btnContainerParams.setMargins(
                    (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0,
                    (int)getResources().getDimension(R.dimen.fragment_btn_horizontal_margin), 0);
            LinearLayout btnFirstContainer = new LinearLayout(getActivity());
            btnFirstContainer.setLayoutParams(btnContainerParams);
            //
            btnNo = new ButtonView(getActivity(), getResources().getString(R.string.no),
                    getResources().getDimension(R.dimen.fragment_button_size),
                    getResources().getDimension(R.dimen.fragment_button_size));
            btnNo.setButtonClickListener(new ButtonView.ButtonClickListener() {
                @Override
                public void ButtonClickListener() {
                    if (playGamesDisableClickListener != null) {
                        playGamesDisableClickListener.PlayGamesDisableClickListener();
                    } else {
                        closeFragment();
                    }
                }
            });
            btnNo.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
            btnFirstContainer.addView(btnNo);
            btnContainer.addView(btnFirstContainer);
            LinearLayout btnSecondContainer = new LinearLayout(getActivity());
            btnSecondContainer.setLayoutParams(btnContainerParams);
            btnYes = new ButtonView(getActivity(), getResources().getString(R.string.yes),
                    getResources().getDimension(R.dimen.fragment_button_size),
                    getResources().getDimension(R.dimen.fragment_button_size));
            btnYes.setButtonClickListener(new ButtonView.ButtonClickListener() {
                @Override
                public void ButtonClickListener() {
                    if (playGamesEnableClickListener != null) {
                        playGamesEnableClickListener.PlayGamesEnableClickListener();
                    } else {
                        closeFragment();
                    }
                }
            });
            btnYes.setBtnHeight(getResources().getDimension(R.dimen.fragment_button_size));
            btnSecondContainer.addView(btnYes);
            btnContainer.addView(btnSecondContainer);
            //
            LinearLayout btnPutCharContainer = view.findViewById(R.id.btn_put_char_container);
            ButtonView btnPutChar = new ButtonView(
                    getActivity(),
                    getResources().getDrawable(R.drawable.icon_hint_put_character), true,
                    getResources().getDrawable(R.drawable.icon_coin_put_character), false,
                    getResources().getDimension(R.dimen.game_activity_hint_button_width),
                    getResources().getDimension(R.dimen.game_activity_hint_button_height));
            btnPutChar.setButtonClickable(false);
            btnPutCharContainer.addView(btnPutChar);
            //
            LinearLayout btnRemoveCharsContainer = view.findViewById(R.id.btn_remove_chars_container);
            ButtonView btnRemoveChars = new ButtonView(
                    getActivity(),
                    getResources().getDrawable(R.drawable.icon_coin_remove_characters), false,
                    getResources().getDrawable(R.drawable.icon_hint_remove_characters), true,
                    getResources().getDimension(R.dimen.game_activity_hint_button_width),
                    getResources().getDimension(R.dimen.game_activity_hint_button_height));
            btnRemoveChars.setButtonClickable(false);
            btnRemoveCharsContainer.addView(btnRemoveChars);
            //
            LinearLayout btnAskFriendContainer = view.findViewById(R.id.btn_ask_friend_container);
            ButtonView btnAskFriend = new ButtonView(
                    getActivity(), getResources().getDrawable(R.drawable.icon_ask_friend),
                    true,
                    getResources().getDimension(R.dimen.game_activity_button_size),
                    getResources().getDimension(R.dimen.game_activity_button_size));
            btnAskFriend.setButtonClickable(false);
            btnAskFriendContainer.addView(btnAskFriend);
            //
            LinearLayout btnStoreContainer = view.findViewById(R.id.btn_store_container);
            ButtonView btnStore = new ButtonView(
                    getActivity(), getResources().getDrawable(R.drawable.icon_store),
                    true,
                    getResources().getDimension(R.dimen.game_activity_button_size),
                    getResources().getDimension(R.dimen.game_activity_button_size));
            btnStore.setButtonClickable(false);
            btnStoreContainer.addView(btnStore);
            //
            LinearLayout outsideLayout = view.findViewById(R.id.fragment_outside_layout);
            outsideLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            LinearLayout insideLayout = view.findViewById(R.id.fragment_inside_layout);
            insideLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return view;
        }

        private void closeFragment() {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }

        @SuppressWarnings("All")
        public interface PlayGamesDisableClickListener {
            public void PlayGamesDisableClickListener();
        }

        private PlayGamesDisableClickListener playGamesDisableClickListener;

        public void setPlayGamesDisableClickListener(PlayGamesDisableClickListener playGamesDisableClickListener) {
            this.playGamesDisableClickListener = playGamesDisableClickListener;
        }

        @SuppressWarnings("All")
        public interface PlayGamesEnableClickListener {
            public void PlayGamesEnableClickListener();
        }

        private PlayGamesEnableClickListener playGamesEnableClickListener;

        public void setPlayGamesEnableClickListener(PlayGamesEnableClickListener playGamesEnableClickListener) {
            this.playGamesEnableClickListener = playGamesEnableClickListener;
        }

    }
