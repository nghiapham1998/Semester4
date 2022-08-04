package fpt.aptech.projectcard.ui.contactus;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import fpt.aptech.projectcard.R;
import fpt.aptech.projectcard.session.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactUsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //contact us
    View view;
    EditText mailAdminReceiver,edTitle,edContent;
    Button btnSend;

    public ContactUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        mailAdminReceiver = view.findViewById(R.id.editEmailReceiver);
        edTitle = view.findViewById(R.id.editTitleMail);
        edContent = view.findViewById(R.id.editContentMail);
        btnSend = view.findViewById(R.id.btnSendMail);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] address = "onetouch@gmail.com".split(",");
                String title = edTitle.getText().toString().trim();
                String content = edContent.getText().toString().trim();
                if (validateSendmail(title,content)){
                    sendMail(address,title,content);
                    edTitle.setText("");
                    edContent.setText("");
                    edTitle.setError(null);
                    edContent.setError(null);
                }
            }
        });

        return view;
    }

    private void sendMail(String[] addresses, String subject, String content) {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,content);
        intent.setType("message/rfc882");
        startActivity(Intent.createChooser(intent,"Send Email"));
    }

    //validate
    private boolean validateSendmail(String title,String content) {

        if (TextUtils.isEmpty(title)) {
            edTitle.setError("Please add a title");
            edTitle.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            edContent.setError("Please writing something");
            edContent.requestFocus();
            return false;
        }
        return true;
    }
}