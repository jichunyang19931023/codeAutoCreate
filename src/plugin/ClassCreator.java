package plugin;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import plugin.util.FileUtil;

import java.util.List;
import java.util.Map;

public class ClassCreator {

    public static void createServiceFile(PsiDirectory workDir, Project project, PsiClass aClass, Map<String, String> configMap, List<String> warnInfos){
        String serviceName = aClass.getName() + "Service";
        String servicePath = configMap.get("service");
        String content = "package " + servicePath + ";\n\n" +
                "import org.springframework.stereotype.Service;\n\n" +
                "@Service\n" +
                "public interface " + serviceName + "{\n\n}";
        // 在service包中创建service文件
        WriteCommandAction.runWriteCommandAction(project, () ->
                FileUtil.createFile(project, workDir, serviceName, servicePath, content, warnInfos));
        System.out.println("service类创建完成");
    }

    public static void createServiceImplFile(PsiDirectory workDir, Project project, PsiClass aClass, Map<String, String> configMap, List<String> warnInfos){
        String serviceImplName = aClass.getName() + "ServiceImpl";
        String servicePath = configMap.get("service");
        String serviceImplPath = configMap.get("serviceImpl");
        String content = "package " + serviceImplPath + ";\n\n" +
                "import " + servicePath + "." + aClass.getName() + "Service;\n" +
                "import org.springframework.stereotype.Service;\n\n" +
                "@Service\n" +
                "public class " + serviceImplName + " implements " + aClass.getName() + "Service{\n\n}";
        // 在service实现包中创建service实现文件
        WriteCommandAction.runWriteCommandAction(project, () ->
                FileUtil.createFile(project, workDir, serviceImplName, serviceImplPath, content, warnInfos));
        System.out.println("serviceImpl类创建完成");
    }

    public static void createControllerFile(PsiDirectory workDir, Project project, PsiClass aClass, Map<String, String> configMap, List<String> warnInfos){
        String controllerName = aClass.getName() + "Controller";
        String controllerPath = configMap.get("controller");
        String content = "package " + controllerPath + ";\n\n" +
                "import org.springframework.web.bind.annotation.RestController;\n\n" +
                "@RestController\n" +
                "public class " + controllerName + "{\n\n}";
        // 在controller包中创建controller文件
        WriteCommandAction.runWriteCommandAction(project, () ->
                FileUtil.createFile(project, workDir, controllerName, controllerPath, content, warnInfos));
        System.out.println("controller类创建完成");
    }


    public static void createRepositoryFile(PsiDirectory workDir, Project project, PsiClass aClass, Map<String, String> configMap, List<String> warnInfos){
        String repositoryName = aClass.getName() + "Repository";
        String repositoryPath = configMap.get("repository");
        String entityImport = repositoryPath.replace("repository", aClass.getName());
        String content = "package " + repositoryPath + ";\n\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import " + entityImport + ";\n" +
                "public interface " + repositoryName + " extends JpaRepository<" + aClass.getName() + ",Long> {\n\n}";
        // 在controller包中创建controller文件
        WriteCommandAction.runWriteCommandAction(project, () ->
                FileUtil.createFile(project, workDir, repositoryName, repositoryPath, content, warnInfos));
        System.out.println("repository类创建完成");
    }
}
