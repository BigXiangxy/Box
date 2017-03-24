package com.box.mapmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ZoomControls;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.box.lib.mvp.view.BaseActivity;
import com.supermap.data.Environment;
import com.supermap.data.Maps;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;

/**
 * Created by Administrator on 2017/3/10 0010.
 */
@Route(path = "/map/main")
public class MapMainActivity extends BaseActivity {

    private MapControl m_mapcontrol = null;
    private Workspace m_workspace;
    private MapView m_mapView;
    private ZoomControls m_zoom;
    String rootPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置一些系统需要用到的路径
        Environment.setLicensePath(rootPath + "/SuperMap/license/");
        Environment.setTemporaryPath(rootPath + "/SuperMap/temp/");
        Environment.setWebCacheDirectory(rootPath + "/SuperMap/WebCatch");
        //组件功能必须在Environment初始化之后才能调用
        Environment.initialization(this);
        //如果机器中默认不包括需要显示的字体，可以把相关字体文件放在参数所代表的路径中。
        // 例如，如果需要显示阿拉伯文字（若机器中原先不包括相关字体文件），可以把需要的字体文件放在参数所代表的路径中。
        Environment.setFontsPath(rootPath + "/SuperMap/fonts/");
        setContentView(R.layout.map_activity_main);
        m_zoom = (ZoomControls) findViewById(R.id.zoomControls1);
        m_mapView = (MapView) findViewById(R.id.Map_view);
        //打开工作空间
        m_workspace = new Workspace();
        WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
//        info.setServer(rootPath + "/SampleData/GeometryInfo/World.smwu");
        info.setServer(rootPath + "/HSFXMap/Data/Map/china.sxwu");
        info.setType(WorkspaceType.SXWU);
        m_workspace.open(info);
        //将地图显示控件和工作空间关联
        m_mapcontrol = m_mapView.getMapControl();
        m_mapcontrol.getMap().setWorkspace(m_workspace);
        //打开工作空间中的第二幅地图
        Maps maps = m_workspace.getMaps();
        for (int i=0;i<maps.getCount();i++)
            Log.e("name-->","--->"+maps.get(i));
        String mapName = maps.get(0);
        m_mapcontrol.getMap().open(mapName);

        m_zoom.setIsZoomInEnabled(true);
        m_zoom.setIsZoomOutEnabled(true);
        //放大按钮
        m_zoom.setOnZoomInClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                m_mapcontrol.getMap().zoom(2);
                m_mapcontrol.getMap().refresh();
            }
        });
        //缩小按钮
        m_zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                m_mapcontrol.getMap().zoom(0.5);
                m_mapcontrol.getMap().refresh();
            }
        });
        m_mapcontrol.getMap().refresh();
    }
}
