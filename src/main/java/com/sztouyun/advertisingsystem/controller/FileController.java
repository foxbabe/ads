package com.sztouyun.advertisingsystem.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;

@Profile({"local","dev","test","stage"})
@Controller
public class FileController extends BaseController{
    @RequestMapping("image/{id}")
    public String index(@PathVariable String id,HttpServletResponse response) {
        try {
            // 设置浏览器不缓存本页
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Expires", "0");
            // 输出验证码给客户端
            response.setContentType("image/jpeg");
            BufferedImage bim = createImage(id,1366,768);
            ImageIO.write(bim, "JPEG", response.getOutputStream());

        } catch (Exception e) {
        }
        return null;
    }

    private BufferedImage createImage(String str, Integer width, Integer height) throws Exception {
        Font font =new Font(Font.SANS_SERIF,Font.PLAIN,height/5);
        // 创建图片
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics g = image.getGraphics();
        g.setClip(0, 0, width, height);
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);// 先用黑色填充整张图片,也就是背景
        g.setColor(Color.black);// 在换成黑色
        g.setFont(font);// 设置画笔字体
        /** 用于获得垂直居中y */
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics(font);
        int ascent = fm.getAscent();
        int descent = fm.getDescent();
        int y = (clip.height - (ascent + descent)) / 2 + ascent;
        g.drawString(str, width/10, y);// 画出字符串
        g.dispose();
        return image;
    }
}
