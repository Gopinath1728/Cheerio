package com.example.cheerio.ui.profile.my_profile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cheerio.R;
import com.example.cheerio.common.Common;
import com.example.cheerio.databinding.FragmentMyProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MyProfileFragment extends Fragment {

    private FragmentMyProfileBinding binding;
    private MyProfileViewModel viewModel;

    private BottomNavigationView nav_view;

    ImageView change_dp;

    LinearLayout user_box_name, user_box_dob, user_box_gender, user_box_phone, user_box_address, user_box_height, user_box_weight, user_box_alcohol, user_box_smoke;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nav_view = getActivity().findViewById(R.id.nav_view);
        nav_view.setVisibility(View.GONE);

        //setting onClick listeners
        binding.imgMyProfBack.setOnClickListener(backClicked);
        binding.changeDp.setOnClickListener(dpChange_clicked);
        binding.userBoxName.setOnClickListener(name_change_click);
        binding.userBoxDob.setOnClickListener(dob_change_click);
        binding.userBoxGender.setOnClickListener(gender_change_click);
        binding.userBoxPhone.setOnClickListener(phone_change_click);
        binding.userBoxAddress.setOnClickListener(address_change_click);
        binding.userBoxHeight.setOnClickListener(height_change_click);
        binding.userBoxWeight.setOnClickListener(weight_change_click);
        binding.userBoxAlcohol.setOnClickListener(alcohol_change_click);
        binding.userBoxSmoke.setOnClickListener(smoke_change_click);


        viewModel.getMutableError().observe(getViewLifecycleOwner(), s -> Snackbar.make(root, s, Snackbar.LENGTH_SHORT).show());

        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), user_model -> {
            if (user_model != null) {

                //filling the profile data

                if (user_model.getImage() != null)
                    Glide.with(getActivity()).load(user_model.getImage()).into(binding.imgUser);

                if (user_model.getName() != null)
                    binding.txtUserName.setText(new StringBuilder(user_model.getName()));

                if (user_model.getBirthday() != null)
                    binding.txtUserBirthday.setText(new StringBuilder(user_model.getBirthday()));

                if (user_model.getGender() != null)
                    binding.txtUserGender.setText(new StringBuilder(user_model.getGender()));

                if (user_model.getPhone() != null)
                    binding.txtUserPhone.setText(new StringBuilder(user_model.getPhone()));

                if (user_model.getEmail() != null)
                    binding.txtUserEmail.setText(new StringBuilder(user_model.getEmail()));

                if (user_model.getAddress() != null)
                    binding.txtUserAddress.setText(new StringBuilder(user_model.getAddress()));

                if (user_model.getHeight() != null)
                    binding.txtUserHeight.setText(new StringBuilder(user_model.getHeight()));

                if (user_model.getWeight() != null)
                    binding.txtUserWeight.setText(new StringBuilder(user_model.getWeight()));

                if (user_model.getAlcohol() != null)
                    binding.txtAlcohol.setText(new StringBuilder(user_model.getAlcohol()));

                if (user_model.getSmoking() != null)
                    binding.txtSmoking.setText(new StringBuilder(user_model.getSmoking()));

            }
        });


        return root;
    }

    View.OnClickListener dpChange_clicked = v -> {

    };

    View.OnClickListener name_change_click = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_dialog, null);
        TextView txt_update = (TextView) itemView.findViewById(R.id.txt_update);
        EditText edt_update_data = (EditText) itemView.findViewById(R.id.edt_update_data);
        Button btn_update = (Button) itemView.findViewById(R.id.btn_update);
        txt_update.setText(new StringBuilder("Update Name"));
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();


        btn_update.setOnClickListener(v1 -> {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(Common.user_model.getUid())
                    .update("name", edt_update_data.getText().toString())
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });

        });
    };

    View.OnClickListener dob_change_click = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_dialog, null);
        TextView txt_update = (TextView) itemView.findViewById(R.id.txt_update);
        EditText edt_update_data = (EditText) itemView.findViewById(R.id.edt_update_data);
        edt_update_data.setCursorVisible(false);
        edt_update_data.setClickable(false);
        edt_update_data.setFocusable(false);
        edt_update_data.setFocusableInTouchMode(false);
        edt_update_data.setHint("Click to show calendar");
        Button btn_update = (Button) itemView.findViewById(R.id.btn_update);
        txt_update.setText(new StringBuilder("Update Birthday"));
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();

        final Calendar myCalendar = Calendar.getInstance();


        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            SimpleDateFormat format = new SimpleDateFormat("dd/MMMM/yyyy", Locale.getDefault());
            String date1 = format.format(myCalendar.getTime());
            edt_update_data.setText(new StringBuilder(date1.replace("/", " ")));
        };


        edt_update_data.setOnClickListener(view -> {
            new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();


        });


        btn_update.setOnClickListener(v1 -> {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(Common.user_model.getUid())
                    .update("birthday", edt_update_data.getText().toString())
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });
    };

    View.OnClickListener gender_change_click = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_dialog, null);
        TextView txt_update = (TextView) itemView.findViewById(R.id.txt_update);
        EditText edt_update_data = (EditText) itemView.findViewById(R.id.edt_update_data);
        edt_update_data.setCursorVisible(false);
        edt_update_data.setClickable(false);
        edt_update_data.setFocusable(false);
        edt_update_data.setFocusableInTouchMode(false);
        edt_update_data.setHint("Select");
        Button btn_update = (Button) itemView.findViewById(R.id.btn_update);
        txt_update.setText(new StringBuilder("Update Gender"));
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] gender = {"Male", "Female", "Other"};

        edt_update_data.setOnClickListener(v12 -> {
            AlertDialog.Builder genderBuilder = new AlertDialog.Builder(getActivity());
            genderBuilder.setItems(gender, (dialog1, which) -> {
                switch (which) {
                    case 0:
                        edt_update_data.setText(new StringBuilder("Male"));
                        break;
                    case 1:
                        edt_update_data.setText(new StringBuilder("Female"));
                        break;
                    case 2:
                        edt_update_data.setText(new StringBuilder("Other"));
                        break;
                }
            });
            AlertDialog genderDialog = genderBuilder.create();
            genderDialog.show();
        });


        btn_update.setOnClickListener(v1 -> {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(Common.user_model.getUid())
                    .update("gender", edt_update_data.getText().toString())
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });
    };

    View.OnClickListener phone_change_click = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_dialog, null);
        TextView txt_update = (TextView) itemView.findViewById(R.id.txt_update);
        EditText edt_update_data = (EditText) itemView.findViewById(R.id.edt_update_data);
        edt_update_data.setInputType(InputType.TYPE_CLASS_NUMBER);
        Button btn_update = (Button) itemView.findViewById(R.id.btn_update);
        txt_update.setText(new StringBuilder("Update Phone"));
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();


        btn_update.setOnClickListener(v1 -> {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(Common.user_model.getUid())
                    .update("phone", edt_update_data.getText().toString())
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });
    };

    View.OnClickListener address_change_click = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_dialog, null);
        TextView txt_update = (TextView) itemView.findViewById(R.id.txt_update);
        EditText edt_update_data = (EditText) itemView.findViewById(R.id.edt_update_data);
        Button btn_update = (Button) itemView.findViewById(R.id.btn_update);
        txt_update.setText(new StringBuilder("Update Address"));
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();


        btn_update.setOnClickListener(v1 -> {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(Common.user_model.getUid())
                    .update("address", edt_update_data.getText().toString())
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });
    };

    View.OnClickListener height_change_click = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_dialog, null);
        TextView txt_update = (TextView) itemView.findViewById(R.id.txt_update);
        EditText edt_update_data = (EditText) itemView.findViewById(R.id.edt_update_data);
        edt_update_data.setInputType(InputType.TYPE_CLASS_NUMBER);
        Button btn_update = (Button) itemView.findViewById(R.id.btn_update);
        txt_update.setText(new StringBuilder("Update Height"));
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();


        btn_update.setOnClickListener(v1 -> {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(Common.user_model.getUid())
                    .update("height", edt_update_data.getText().toString())
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });
    };

    View.OnClickListener weight_change_click = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_dialog, null);
        TextView txt_update = (TextView) itemView.findViewById(R.id.txt_update);
        EditText edt_update_data = (EditText) itemView.findViewById(R.id.edt_update_data);
        edt_update_data.setInputType(InputType.TYPE_CLASS_NUMBER);
        Button btn_update = (Button) itemView.findViewById(R.id.btn_update);
        txt_update.setText(new StringBuilder("Update Weight"));
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();


        btn_update.setOnClickListener(v1 -> {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(Common.user_model.getUid())
                    .update("weight", edt_update_data.getText().toString())
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });
    };

    View.OnClickListener alcohol_change_click = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_dialog, null);
        TextView txt_update = (TextView) itemView.findViewById(R.id.txt_update);
        EditText edt_update_data = (EditText) itemView.findViewById(R.id.edt_update_data);
        edt_update_data.setCursorVisible(false);
        edt_update_data.setClickable(false);
        edt_update_data.setFocusable(false);
        edt_update_data.setFocusableInTouchMode(false);
        edt_update_data.setHint("Select");
        Button btn_update = (Button) itemView.findViewById(R.id.btn_update);
        txt_update.setText(new StringBuilder("Update Alcohol Consumption"));
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] gender = {"Rarely", "Frequently", "Never"};

        edt_update_data.setOnClickListener(v12 -> {
            AlertDialog.Builder alcoholBuilder = new AlertDialog.Builder(getActivity());
            alcoholBuilder.setItems(gender, (dialog1, which) -> {
                switch (which) {
                    case 0:
                        edt_update_data.setText(new StringBuilder("Rarely"));
                        break;
                    case 1:
                        edt_update_data.setText(new StringBuilder("Frequently"));
                        break;
                    case 2:
                        edt_update_data.setText(new StringBuilder("Never"));
                        break;
                }
            });
            AlertDialog genderDialog = alcoholBuilder.create();
            genderDialog.show();
        });


        btn_update.setOnClickListener(v1 -> {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(Common.user_model.getUid())
                    .update("alcohol", edt_update_data.getText().toString())
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });
    };

    View.OnClickListener smoke_change_click = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.profile_update_dialog, null);
        TextView txt_update = (TextView) itemView.findViewById(R.id.txt_update);
        EditText edt_update_data = (EditText) itemView.findViewById(R.id.edt_update_data);
        edt_update_data.setCursorVisible(false);
        edt_update_data.setClickable(false);
        edt_update_data.setFocusable(false);
        edt_update_data.setFocusableInTouchMode(false);
        edt_update_data.setHint("Select");
        Button btn_update = (Button) itemView.findViewById(R.id.btn_update);
        txt_update.setText(new StringBuilder("Update Smoking"));
        builder.setView(itemView);
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] gender = {"Rarely", "Frequently", "Never"};

        edt_update_data.setOnClickListener(v12 -> {
            AlertDialog.Builder smokeBuilder = new AlertDialog.Builder(getActivity());
            smokeBuilder.setItems(gender, (dialog1, which) -> {
                switch (which) {
                    case 0:
                        edt_update_data.setText(new StringBuilder("Rarely"));
                        break;
                    case 1:
                        edt_update_data.setText(new StringBuilder("Frequently"));
                        break;
                    case 2:
                        edt_update_data.setText(new StringBuilder("Never"));
                        break;
                }
            });
            AlertDialog genderDialog = smokeBuilder.create();
            genderDialog.show();
        });


        btn_update.setOnClickListener(v1 -> {
            FirebaseFirestore.getInstance().collection("Users")
                    .document(Common.user_model.getUid())
                    .update("smoking", edt_update_data.getText().toString())
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    });
        });
    };


    View.OnClickListener backClicked = v -> {
        getActivity().onBackPressed();
        nav_view.setVisibility(View.VISIBLE);
    };
}