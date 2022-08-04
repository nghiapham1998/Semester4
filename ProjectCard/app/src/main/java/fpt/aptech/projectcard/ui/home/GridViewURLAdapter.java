package fpt.aptech.projectcard.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fpt.aptech.projectcard.MainActivity;
import fpt.aptech.projectcard.R;
import fpt.aptech.projectcard.callApiService.ApiService;
import fpt.aptech.projectcard.domain.UrlProduct;
import fpt.aptech.projectcard.retrofit.RetrofitService;
import fpt.aptech.projectcard.session.SessionManager;
import fpt.aptech.projectcard.ui.profile.ProfileFragment;
import fpt.aptech.projectcard.ui.social.SocialFragment;
import fpt.aptech.projectcard.ui.social.UpdateUrlFragment;

public class GridViewURLAdapter extends ArrayAdapter<UrlProduct> {
    FragmentManager fragmentManager;

    public GridViewURLAdapter(@NonNull Context context, ArrayList<UrlProduct> urlProductArrayList, FragmentManager fragmentManager) {
        super(context,0, urlProductArrayList);
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }
        UrlProduct urlProduct = getItem(position);
        ImageView img = listItemView.findViewById(R.id.imgGridItem);
        try {
            //avatar
            Bitmap bitmap1 = BitmapFactory.decodeStream((InputStream)new URL(urlProduct.getLinkType().getLinkImage()).getContent());
            img.setImageBitmap(bitmap1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (urlProduct.getLinkType().getId() != 6 && urlProduct.getLinkType().getId() != 7 ){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + urlProduct.getUrl()));
                }
                if (urlProduct.getLinkType().getId() == 1){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/" + urlProduct.getUrl()));
                }
                if (urlProduct.getLinkType().getId() == 2){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/" + urlProduct.getUrl()));
                }
                if (urlProduct.getLinkType().getId() == 3){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/" + urlProduct.getUrl()));
                }
                if (urlProduct.getLinkType().getId() == 4){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/" + urlProduct.getUrl()));
                }
                if (urlProduct.getLinkType().getId() == 5){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + urlProduct.getUrl()));
                }
                if (urlProduct.getLinkType().getId() == 6){
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + urlProduct.getUrl()));
                }
                if (urlProduct.getLinkType().getId() == 7){
                    intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + urlProduct.getUrl()));
                }
                getContext().startActivity(intent);
            }
        });
        img.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(),img);
                popup.inflate(R.menu.popup_menu_item);

                Menu menuOpts = popup.getMenu();
                menuOpts.getItem(0).setTitle("⭐" + urlProduct.getName());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return menuItemClicked(item);
                    }
                });

                popup.show();
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            private boolean menuItemClicked(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuItem_Name:
                        Toast.makeText(getContext(), urlProduct.getName(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuItem_edit:
                        Toast.makeText(getContext(), "Update Url layout", Toast.LENGTH_SHORT).show();
                        SessionManager.setSaveUrlProduct(urlProduct);
                        fragmentManager.beginTransaction().replace(R.id.fl_content_home, new UpdateUrlFragment())
                                .addToBackStack(null).commit();
                        break;
                    case R.id.menuItem_drop:
                        ApiService apiService = RetrofitService.proceedToken().create(ApiService.class);
                        try {
                            apiService.deleteUrlById(urlProduct.getId()).execute().body();
                            Toast.makeText(getContext(), "dropped url", Toast.LENGTH_SHORT).show();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                        break;
                }
                return true;
            }

        });
        return listItemView;
    }
}
