package com.china.lhf.app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.china.lhf.app.R;
import com.china.lhf.app.entity.Favorites;
import com.china.lhf.app.entity.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 收藏适配器
 */
public class MyFavoriteAdapter extends SimpleAdapter<Favorites> {

    private FavoriteListener favoriteListener;

    public MyFavoriteAdapter(Context context, List<Favorites> datas, FavoriteListener favoriteListener) {
        super(datas,context, R.layout.item_my_favorite);
        this.favoriteListener = favoriteListener;
    }

    @Override
    protected void convert(BaseViewHoder viewHolder, final Favorites favorites) {
        final Wares wares = favorites.getWares();
        SimpleDraweeView favoriteSDV = (SimpleDraweeView) viewHolder
                .getView(R.id.favorite_sdv);
        favoriteSDV.setImageURI(Uri.parse(wares.getImgUrl()));
        viewHolder.getTextView(R.id.favorite_title_tv).setText(wares.getName());
        viewHolder.getTextView(R.id.favorite_price_tv).setText("￥" + wares.getPrice() + "");

        Button removeBtn = viewHolder.getButton(R.id.remove_btn);
        Button simpleBtn = viewHolder.getButton(R.id.simple_btn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoriteListener.deleteFavorite(wares.getId());
            }
        });

    }

    public interface FavoriteListener {
        public void deleteFavorite(Long id);
    }

}
