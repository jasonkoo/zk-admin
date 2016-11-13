package com.lenovo.wanba.zk.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lenovo.czlib.nodex.conf.ZKProperties;
import com.lenovo.wanba.zk.model.ZkNode;
import com.lenovo.wanba.zk.services.ZkBaseService;
import com.lenovo.wanba.zk.util.Constant;

@Controller
@RequestMapping(value = "/configAjax")
public class ConfigAjaxController {

    private static final Logger LOG = Logger.getLogger(ConfigAjaxController.class);

    private static final String ENCODE = "UTF-8";

    private static final String SUCC = "succ";

    private static final int READ_BYTE = 1024;

    @Autowired
    private ZkBaseService zkAjaxService;

    @Value("${${serverName}.connectString}")
    private String connectString;

    /**
     * 页面初始化
     * 
     * @param model
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    @RequestMapping(method = RequestMethod.GET)
    public String show(Model model) throws KeeperException, InterruptedException {
        List<ZkNode> znodeList = zkAjaxService.getChildZNodes("/");
        model.addAttribute("znodeList", znodeList);
        return Constant.PAGE_COMMON;
    }

    /**
     * 取得节点信息
     * 
     * @param path
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    @RequestMapping(value = "/getZNode", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, ? extends Object> getZNode(@RequestParam String path) throws KeeperException,
            InterruptedException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String msg = SUCC;

        ZkNode znode = zkAjaxService.getZNode(path);
        resultMap.put("znode", znode);

        resultMap.put("msg", msg);

        return resultMap;
    }

    /**
     * 取得子节点信息
     * 
     * @param path
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    @RequestMapping(value = "/getChildren", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, ? extends Object> getChildren(@RequestParam String path) throws KeeperException,
            InterruptedException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String msg = SUCC;
        List<ZkNode> znodeList = zkAjaxService.getChildZNodes(path);
        resultMap.put("znodeList", znodeList);

        resultMap.put("msg", msg);

        return resultMap;
    }

    /**
     * 更新节点信息
     * 
     * @param path
     * @param data
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    @RequestMapping(value = "/updateZNode", method = { RequestMethod.POST })
    @ResponseBody
    public String updateZNode(@RequestParam("path") String path, @RequestParam("data") String data)
            throws KeeperException, InterruptedException {
        LOG.info("path >>" + path + "; data >>" + data);
        zkAjaxService.updateZNode(path, data);

        return SUCC;
    }

    /**
     * 删除节点信息
     * 
     * @param path
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    @RequestMapping(value = "/deleteZNode", method = RequestMethod.GET)
    @ResponseBody
    public String deleteZNode(@RequestParam String path) throws InterruptedException, KeeperException {
        zkAjaxService.deleteZNode(path);

        return SUCC;
    }

    /**
     * 新增节点信息
     * 
     * @param path
     * @param data
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    @RequestMapping(value = "/addZNode", method = { RequestMethod.POST })
    @ResponseBody
    public String addZNode(@RequestParam String path, @RequestParam String data) throws KeeperException,
            InterruptedException {
        LOG.info("path >>" + path + "; data >>" + data);
        zkAjaxService.addZNode(path, data);

        return SUCC;
    }

    private String getChindNode(int depth, String path) throws Exception {
        StringBuffer sb = new StringBuffer();
        List<ZkNode> list = zkAjaxService.getChildZNodes(path);
        if (list == null || list.size() == 0) {
            return "";
        }
        for (ZkNode node : list) {
            for (int i = 0; i < depth; i++) {
                sb.append("\t");
            }
            sb.append(node.getPath()).append("=").append(node.getData()).append("\r\n");
            int nextDepth = depth + 1;
            sb.append(getChindNode(nextDepth, node.getPath()));
        }
        return sb.toString();
    }

    @RequestMapping(value = "/exportNode")
    @ResponseBody
    public String exportNode(@RequestParam String path, HttpServletResponse response) throws Exception {
        try {
            // 取得文件名。
            String filename = "content_node" + path.replace("/", "_") + ".txt";
            String context = getChindNode(0, path);
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new ByteArrayInputStream(context.getBytes(ENCODE)));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.addHeader("Content-Length", "" + context.getBytes(ENCODE).length);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return SUCC;
    }

    @RequestMapping(value = "goUpload")
    public String goUpload(Model model) {
        return "common/upload";
    }

    @RequestMapping(value = "/importNode")
    @ResponseBody
    public String importNode(HttpServletRequest request, @RequestParam("fileUpload") MultipartFile fileUpload,
            @RequestParam("path") String path, @RequestParam("overwrite") boolean overwrite) {
        if (!fileUpload.isEmpty()) {
            String dirPath = "/tmp";
            File dirFile = new File(dirPath);
            if (!dirFile.exists() && !dirFile.mkdirs()) {
                return SUCC;
            }

            File uploadFile = new File(dirFile, fileUpload.getOriginalFilename());

            FileOutputStream fos = null;
            InputStream is = null;
            try {
                is = fileUpload.getInputStream();
                fos = new FileOutputStream(uploadFile);
                byte[] tmp = new byte[READ_BYTE];
                int len = -1;
                while ((len = is.read(tmp)) != -1) {
                    fos.write(tmp, 0, len);
                }
                fos.flush();
                importDataFromFile(path, overwrite, uploadFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return SUCC;
    }

    private void importDataFromFile(String path, boolean overwrite, File uploadFile) throws Exception {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(uploadFile), ENCODE));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.trim().length() > 0) {
                    addNodeByLineStr(path, overwrite, tempString);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    private void addNodeByLineStr(String path, boolean overwrite, String lineStr) throws KeeperException,
            InterruptedException {
        String tmpPath = "";
        String[] datas = lineStr.split("=", 2);
        if (lineStr.startsWith("/")) {
            tmpPath = datas[0];
        } else {
            tmpPath = path + "/" + datas[0];
        }
        ZkNode zkNode = zkAjaxService.getZNode(tmpPath);
        if (zkNode == null) {
            String[] tmps = tmpPath.split("/");
            StringBuffer parentPath = new StringBuffer();
            for (int i = 1; i < tmps.length - 1; i++) {
                parentPath.append("/").append(tmps[i]);
                ZkNode zkNodeP = zkAjaxService.getZNode(parentPath.toString());
                if (zkNodeP == null) {
                    zkAjaxService.addZNode(parentPath.toString(), "");
                }
            }
            zkAjaxService.addZNode(tmpPath, datas[1]);
        } else if (overwrite) {
            zkAjaxService.updateZNode(tmpPath, datas[1]);
        }
    }

    @RequestMapping(value = "goAddNodexConfig")
    public String goAddNodexConfig(Model model) {
        File targetFile = new File("/tmp/" + System.currentTimeMillis());
        try {
            ZKProperties.export(connectString, targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), ENCODE));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString).append("\r\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        targetFile.delete();
        model.addAttribute("data", sb.toString());
        return "common/add_nodex_config";
    }

    @RequestMapping(value = "addNodexConfig")
    @ResponseBody
    public String addNodexConfig(@RequestParam("data") String data, @RequestParam("overwrite") boolean overwrite) {
        File configFile = createFile(data);
        try {
            // 备份数据
            File targetFile = new File("/tmp/nodex_conf." + System.currentTimeMillis());
            try {
                ZKProperties.export(connectString, targetFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ZKProperties.inport(connectString, configFile, overwrite);
            configFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCC;
    }

    private File createFile(String data) {
        File file = new File("/tmp/" + System.currentTimeMillis());
        BufferedWriter buf = null;
        try {
            buf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), ENCODE));
            buf.write(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}
