package plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.PropertiesUtil;
import com.intellij.psi.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class ConfigDispose {

    public static Map<String, String> getConfigPath(Project project, PsiClass aClass) {
        Map<String, String> configPath = new HashMap<>();
        String basePath = project.getBasePath();
        Path path = Paths.get(basePath + "/class-auto-create-config.properties");
        // 如果配置文件已经存在，则读取默认配置项
        // 如果修改配置文件，需要重新 Build 后再运行插件才可生效
        if (path.toFile().exists()) {
            try {
                Map<String, String> properties = PropertiesUtil.loadProperties(new FileReader(path.toFile()));
                properties.forEach((k, v) -> {
                    switch (k) {
                        case "path.service":
                            configPath.put("service", String.valueOf(v));
                            break;
                        case "path.service.impl":
                            configPath.put("serviceImpl", String.valueOf(v));
                            break;
                        case "path.controller":
                            configPath.put("controller", String.valueOf(v));
                            break;
                        case "path.repository":
                            configPath.put("repository", String.valueOf(v));
                            break;
                    }
                });
            } catch (IOException e) {
                Messages.showMessageDialog(project, "加载配置文件class-auto-create-config.properties失败：\n" + e.getMessage(), "Plugin Error", Messages.getErrorIcon());
            }
        } else {
            // 配置文件不存在，在项目目录下创建配置文件
            File file = path.toFile();
            try {
                String daoPackage = ((PsiJavaFile) aClass.getContainingFile()).getPackageName();
                String entityPackageName = "";
                if (daoPackage.contains("entity")){
                    entityPackageName = "entity";
                }else if(daoPackage.contains("domain")){
                    entityPackageName = "domain";
                }
                String servicePath = daoPackage.replace(entityPackageName, "service");
                String serviceImplPath = daoPackage.replace(entityPackageName, "service.impl");
                String controllerPath = daoPackage.replace(entityPackageName, "controller");
                String repositoryPath = daoPackage.replace(entityPackageName, entityPackageName + ".repository");
                List<String> configs = Arrays.asList("# 生成service文件的包", "path.service=" + servicePath,
                        "# 生成serviceImpl文件的包", "path.service.impl=" + serviceImplPath,
                        "# 生成controller文件的包", "path.controller=" + controllerPath,
                        "# 生成repository文件的包", "path.repository=" + repositoryPath);
                Files.write(path, configs, StandardOpenOption.CREATE);

                // 设置默认路径
                configPath.put("service", servicePath);
                configPath.put("serviceImpl", serviceImplPath);
                configPath.put("controller", controllerPath);
                configPath.put("repository", repositoryPath);
            } catch (IOException e) {
                Messages.showMessageDialog(project, "创建配置文件class-auto-create-config.properties失败：\n" + e.getMessage(), "Plugin Error", Messages.getErrorIcon());
            }
        }
        return configPath;
    }
}
