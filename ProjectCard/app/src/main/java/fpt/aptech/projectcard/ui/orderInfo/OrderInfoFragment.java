package fpt.aptech.projectcard.ui.orderInfo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fpt.aptech.projectcard.R;
import fpt.aptech.projectcard.callApiService.ApiConstant;
import fpt.aptech.projectcard.callApiService.ApiService;
import fpt.aptech.projectcard.domain.Orders;
import fpt.aptech.projectcard.domain.Product;
import fpt.aptech.projectcard.domain.User;
import fpt.aptech.projectcard.retrofit.RetrofitService;
import fpt.aptech.projectcard.session.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    //check card status;
    TextView cardStatus, buyer, buyAt, activateAt,duration,expired, countdown,extend;
    User getUser;
    Product getProduct;
    List<Orders> ordersListByUser;
    ListView listView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderInfoFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderInfoFragment newInstance(String param1, String param2) {
        OrderInfoFragment fragment = new OrderInfoFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_info, container, false);
        cardStatus = view.findViewById(R.id.txt_card_status);
        buyer = view.findViewById(R.id.txt_Buyer);
        buyAt = view.findViewById(R.id.txt_BuyAt);
        activateAt = view.findViewById(R.id.txt_ActivateAt);
        duration = view.findViewById(R.id.txt_Duration);
        expired = view.findViewById(R.id.txt_Expired);
        countdown = view.findViewById(R.id.txt_countDownDuration);
        extend = view.findViewById(R.id.txt_Extend);
        listView = view.findViewById(R.id.lvOrderHis);

        extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiConstant.BASE_URL_CLIENT + "/Shopping"));
                startActivity(intent);
            }
        });
//        Toast.makeText(getActivity().getApplicationContext(),"Order Info",Toast.LENGTH_LONG).show();
        ApiService apiService = RetrofitService.proceedToken().create(ApiService.class);

        try {
            getProduct = apiService.getProduct(SessionManager.getSaveUsername(), SessionManager.getSaveToken()).execute().body();
            ordersListByUser = apiService.getOrdersByUsername(SessionManager.getSaveUsername()).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cardStatus.setText(cardStatus.getText() + (getProduct.getStatus() == 1?"Active":"Deactive"));
        buyer.setText(buyer.getText() + getProduct.getUser().getFullname());
        duration.setText(duration.getText() + String.valueOf(getProduct.getYear()) + " years");
        String[] create = getProduct.getCreatedAt().toString().split("T");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start =sdf.parse(create[0]);
            Date today = new Date();//new Date to get current date
            Calendar t = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            t.setTime(today);
            c.setTime(start);
            c.add(Calendar.YEAR,getProduct.getYear());
            buyAt.setText(buyAt.getText() + create[0]);
            activateAt.setText(activateAt.getText() + create[0]);
            expired.setText(expired.getText() + sdf.format(c.getTime()));
            Long age = ChronoUnit.DAYS.between(t.toInstant(),c.toInstant());
            countdown.setText(countdown.getText() + age.toString() + "days");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Orders> ordersArrayList = new ArrayList<>(ordersListByUser);
        OrderHisAdapter adapter = new OrderHisAdapter(getActivity(),ordersArrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        return view;
    }

}