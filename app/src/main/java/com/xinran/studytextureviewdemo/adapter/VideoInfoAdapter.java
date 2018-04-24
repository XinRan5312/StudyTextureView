package com.xinran.studytextureviewdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinran.studytextureviewdemo.R;
import com.xinran.studytextureviewdemo.entity.Video;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by houqixin on 2018/4/24.
 */
public class VideoInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<Video> videos;
    private LayoutInflater mLayoutInflater;

    public VideoInfoAdapter(Context context, List<Video> videos) {
        this.mContext = context;
        this.videos = videos;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.fragment_videolist_item, parent, false);
            viewHolder.imge_pic = (ImageView) convertView.findViewById(R.id.imge_pic);
            viewHolder.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Video video = videos.get(position);
        viewHolder.tv_duration.setText(transferLongToDate("HH:mm:ss", video.getDuration()));
        viewHolder.tv_title.setText(video.getTitle());
        viewHolder.tv_type.setText(video.getMimeType());
        Bitmap bitmap = getVideoThumbnail(video.getPath(), 80, 80, MediaStore.Video.Thumbnails.MINI_KIND);
        if (bitmap != null) {
            viewHolder.imge_pic.setImageBitmap(bitmap);
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView imge_pic;
        private TextView tv_duration;
        private TextView tv_title;
        private TextView tv_type;
    }


    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);

    }
}