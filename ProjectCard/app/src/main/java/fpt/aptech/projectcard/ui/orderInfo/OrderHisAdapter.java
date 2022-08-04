package fpt.aptech.projectcard.ui.orderInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fpt.aptech.projectcard.R;
import fpt.aptech.projectcard.domain.Orders;

public class OrderHisAdapter extends BaseAdapter {
    private Context context;
    private List<Orders> ordersListUser;

    public OrderHisAdapter(Context context, List<Orders> ordersListUser) {
        this.context = context;
        this.ordersListUser = ordersListUser;
    }

    @Override
    public int getCount() {
        return ordersListUser.size();
    }

    @Override
    public Object getItem(int position) {
        return ordersListUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);//bơm data vào đây để add
            view = inflater.inflate(R.layout.order_his_item, null);
        }
        ((TextView)view.findViewById(R.id.tvCode)).setText(String.valueOf(ordersListUser.get(position).getId().intValue()));
        ((TextView)view.findViewById(R.id.tvCategory)).setText(ordersListUser.get(position).getCategory().getName());
        ((TextView)view.findViewById(R.id.tvOrderAt)).setText(ordersListUser.get(position).getCreatedAt().split("T")[0]);
        ((TextView)view.findViewById(R.id.tvPrice)).setText(String.valueOf(ordersListUser.get(position).getPrice()) + "$");
        ((TextView)view.findViewById(R.id.tvStatus)).setText(ordersListUser.get(position).getOrder_process().getName());
        return view;
    }
}
