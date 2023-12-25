package com.example.samplestickerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.BindingAdapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samplestickerapp.Models.Friend;
import com.example.samplestickerapp.Models.Users;
import com.example.samplestickerapp.databinding.ActivityFindFriendsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FindFriends extends AppCompatActivity {
    ActivityFindFriendsBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseUser loggedInUser;
    ProgressDialog progressDialog;
    Users user = new Users();
    Users sender = new Users();
    Friend friend_user = new Friend();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        loggedInUser = auth.getCurrentUser();
        progressDialog = new ProgressDialog(FindFriends.this);
        progressDialog.setTitle("Find Friends");
        progressDialog.setMessage("Checking Users");
        progressDialog.setCanceledOnTouchOutside(false);
        binding.setResult(false);

        database.getReference().child("Users").child(loggedInUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sender = snapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.FFbackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindFriends.this,MainActivity.class);
                startActivity(intent);
            }
        });

        binding.FFSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedEmail = binding.FFetSearchFriends.getText().toString();
                if(binding.FFetSearchFriends.toString().isEmpty() || !isEmailValid(binding.FFetSearchFriends.getText().toString())){
                    binding.FFetSearchFriends.setError("Invalid Email!!");
                    return;
                }
                else{
                    progressDialog.show();
                    database.getReference().child("Users").orderByChild("eemail").equalTo(typedEmail)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    progressDialog.dismiss();
                                    if(snapshot.hasChildren()){
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                            user = dataSnapshot.getValue(Users.class);
                                            user.setUserId(dataSnapshot.getKey());
                                            friend_user.setUserId(user.getUserId());
                                            friend_user.setUserName(user.getUserName());
                                            friend_user.setEemail(user.getEemail());
                                            try{ friend_user.setProfilePic(user.getProfilePic());}catch (Exception e){ }
                                            if(!user.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                                                database.getReference().child("PendingRequests").child(user.getUserId()).orderByChild("eemail")
                                                                .equalTo(sender.getEemail()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.hasChildren()){
                                                                    binding.setResult(true);
                                                                    Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.avatar).into(binding.FFprofileImage);
                                                                    binding.FFtvUserName.setText(user.getUserName());
                                                                    binding.FFtvMail.setText(user.getEemail());
                                                                    binding.FFsendRequest.setText("Cancel Friend Request");
                                                                    binding.FFsendRequest.setBackground(AppCompatResources.getDrawable(FindFriends.this,
                                                                            R.drawable.btn_bg_decline));
                                                                    Toast.makeText(FindFriends.this, "yes in pending", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                database.getReference().child("FriendList").child(loggedInUser.getUid()).orderByChild("eemail")
                                                        .equalTo(user.getEemail())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.hasChildren()){
                                                                    binding.setResult(true);
                                                                    Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.avatar).into(binding.FFprofileImage);
                                                                    binding.FFtvUserName.setText(user.getUserName());
                                                                    binding.FFtvMail.setText(user.getEemail());
                                                                    binding.FFsendRequest.setText("Remove Friend");
                                                                    binding.FFsendRequest.setBackground(AppCompatResources.getDrawable(FindFriends.this,
                                                                            R.drawable.btn_bg_decline));
                                                                }
                                                                else{
                                                                    binding.setResult(true);
                                                                    Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.avatar).into(binding.FFprofileImage);
                                                                    binding.FFtvUserName.setText(user.getUserName());
                                                                    binding.FFtvMail.setText(user.getEemail());
                                                                    binding.FFsendRequest.setText("Send Friend Request");
                                                                    binding.FFsendRequest.setBackground(AppCompatResources.getDrawable(FindFriends.this,
                                                                            R.drawable.btn_bg_send_request));
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                database.getReference().child("PendingRequests").child(loggedInUser.getUid()).orderByChild("eemail")
                                                        .equalTo(user.getEemail())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                if(snapshot.hasChildren()){
                                                                    binding.setResult(true);
                                                                    Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.avatar).into(binding.FFprofileImage);
                                                                    binding.FFtvUserName.setText(user.getUserName());
                                                                    binding.FFtvMail.setText(user.getEemail());
                                                                    binding.FFsendRequest.setText("Respond to Friend Request");
                                                                    binding.FFsendRequest.setBackground(AppCompatResources.getDrawable(FindFriends.this,
                                                                            R.drawable.btn_bg_send_request));
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                            }
                                            else{
                                                binding.setResult(false);
                                                binding.FFtvResultInfo.setText("You cannot add yourself as Friend");
                                            }
                                        }
                                    }
                                    else{
                                        binding.setResult(false);
                                        binding.FFtvResultInfo.setText("No Result Found with this Email address");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        });


        binding.FFsendRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Friend friend_seder = new Friend();
                friend_seder.setUserId(sender.getUserId());
                Log.d("MY_TAG","FindFriends : friend_seder Id is set to : "+ friend_seder.getUserId());
                friend_seder.setUserName(sender.getUserName());
                friend_seder.setEemail(sender.getEemail());
                try{ friend_seder.setProfilePic(sender.getProfilePic());}catch (Exception e){ }

                String check = binding.FFsendRequest.getText().toString();
                if(check.equals("Remove Friend")){
                    Dialog dialog = new Dialog(FindFriends.this);
                    dialog.setContentView(R.layout.are_you_sure);
                    TextView dialogtv = (TextView)dialog.findViewById(R.id.dialogtv);
                    dialogtv.setText("Are you Sure you want to Remove '"+ user.getUserName()+"' from your Friend list?");
                    dialog.show();
                    Button Yes = (Button) dialog.findViewById(R.id.dialogYesBtn);
                    Button No = (Button) dialog.findViewById(R.id.dialogNoBtn);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            database.getReference().child("FriendList").child(loggedInUser.getUid()).child(user.getUserId()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference().child("FriendList").child(user.getUserId()).child(loggedInUser.getUid()).removeValue();
                                            binding.FFsendRequest.setText("Send Friend Request");
                                            binding.FFsendRequest.setBackground(AppCompatResources.getDrawable(FindFriends.this,
                                                    R.drawable.btn_bg_send_request));
                                            Toast.makeText(FindFriends.this, "Friend Removed", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
                    No.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                if(check.equals("Send Friend Request")){
                    Dialog dialog = new Dialog(FindFriends.this);
                    dialog.setContentView(R.layout.are_you_sure);
                    TextView dialogtv = (TextView)dialog.findViewById(R.id.dialogtv);
                    dialogtv.setText("Are you Sure you want to Send '"+ user.getUserName()+"' Friend Request?");
                    dialog.show();
                    Button Yes = (Button) dialog.findViewById(R.id.dialogYesBtn);
                    Button No = (Button) dialog.findViewById(R.id.dialogNoBtn);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            database.getReference().child("PendingRequests").child(user.getUserId()).child(loggedInUser.getUid())
                                    .setValue(friend_seder).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(FindFriends.this, "Friend Request Sent", Toast.LENGTH_SHORT).show();
                                            binding.FFsendRequest.setText("Cancel Friend Request");
                                            binding.FFsendRequest.setBackground(AppCompatResources.getDrawable(FindFriends.this,
                                                    R.drawable.btn_bg_decline));
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
                    No.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                if(check.equals("Cancel Friend Request")){
                    Dialog dialog = new Dialog(FindFriends.this);
                    dialog.setContentView(R.layout.are_you_sure);
                    TextView dialogtv = (TextView)dialog.findViewById(R.id.dialogtv);
                    dialogtv.setText("Are you Sure you want to Cancel Friend Request sent to '"+ user.getUserName()+"'");
                    dialog.show();
                    Button Yes = (Button) dialog.findViewById(R.id.dialogYesBtn);
                    Button No = (Button) dialog.findViewById(R.id.dialogNoBtn);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            database.getReference().child("PendingRequests").child(user.getUserId()).child(loggedInUser.getUid())
                                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(FindFriends.this, "Friend Request Cancelled", Toast.LENGTH_SHORT).show();
                                            binding.FFsendRequest.setText("Send Friend Request");
                                            binding.FFsendRequest.setBackground(AppCompatResources.getDrawable(FindFriends.this,
                                                    R.drawable.btn_bg_send_request));
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
                    No.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                if(check.equals("Respond to Friend Request")){
                    Dialog dialog = new Dialog(FindFriends.this);
                    dialog.setContentView(R.layout.are_you_sure);
                    TextView dialogtv = (TextView)dialog.findViewById(R.id.dialogtv);
                    dialogtv.setText("Press Yes to accept Friend Request or No to Decline");
                    dialog.show();
                    Button Yes = (Button) dialog.findViewById(R.id.dialogYesBtn);
                    Button No = (Button) dialog.findViewById(R.id.dialogNoBtn);
                    Yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            database.getReference().child("FriendList").child(friend_seder.getUserId()).child(friend_user.getUserId())
                                    .setValue(friend_user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference().child("FriendList").child(friend_user.getUserId()).child(friend_seder.getUserId())
                                                            .setValue(friend_seder).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            database.getReference().child("PendingRequests").child(friend_seder.getUserId())
                                                                            .child(friend_user.getUserId()).removeValue()
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    Toast.makeText(FindFriends.this, "You have Successfully added "+friend_user.getUserName()
                                                                                            +" to your friend list", Toast.LENGTH_SHORT).show();
                                                                                    binding.FFsendRequest.setText("Remove Friend");
                                                                                    binding.FFsendRequest.setBackground(AppCompatResources.getDrawable(FindFriends.this,
                                                                                            R.drawable.btn_bg_decline));
                                                                                    dialog.dismiss();
                                                                                }
                                                                            });
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
                    No.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            database.getReference().child("PendingRequests").child(friend_seder.getUserId()).child(friend_user.getUserId())
                                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(FindFriends.this, "Friend Request Cancelled", Toast.LENGTH_SHORT).show();
                                            binding.FFsendRequest.setText("Send Friend Request");
                                            binding.FFsendRequest.setBackground(AppCompatResources.getDrawable(FindFriends.this,
                                                    R.drawable.btn_bg_send_request));
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    });
                }
            }
        });


    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}