package plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class MyAnAction extends AnAction {

    private PsiDirectory workDir;
    protected Project project;
    protected PsiDirectory containerDirectory;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        // 获取需要处理的entity类
        PsiClass aClass = this.getEditingClass(anActionEvent);
        if (null == aClass) {
            return;
        }
        // 如果没有被Entity注解，则不生成
        if (null == aClass.getAnnotation("javax.persistence.Entity")) {
            return;
        }

        // 获取当前实体所在目录的上级目录，需要严格按说明中的目录组织，其它目录不考虑
        workDir = aClass.getContainingFile().getContainingDirectory().getParentDirectory();
        Map<String, String> configMap = ConfigDispose.getConfigPath(project, aClass);

        // 自动创建服务文件
        ClassCreator.createServiceFile(workDir, project, aClass, configMap);
        // 自动创建服务实现文件
        ClassCreator.createServiceImplFile(workDir, project, aClass, configMap);
        // 自动创建控制器文件
        ClassCreator.createControllerFile(workDir, project, aClass, configMap);
        // 自动创建repository文件
        ClassCreator.createRepositoryFile(workDir, project, aClass, configMap);
    }

    public PsiClass getEditingClass(AnActionEvent anActionEvent) {
        // 获取当前项目
        project = anActionEvent.getProject();
        if (null == project) {
            return null;
        }
        // 获取当前的编辑器对象
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        if (null == editor) {
            return null;
        }
        // 获取当前编辑的文件
        PsiFile psiFile = PsiDocumentManager.getInstance(anActionEvent.getProject()).getPsiFile(editor.getDocument());
        if (!(psiFile instanceof PsiJavaFile)) {
            return null;
        }
        PsiJavaFile javaFile = (PsiJavaFile) psiFile;
        // 获取JavaFile中的Class（一个文件中可能会定义有多个Class，因此返回的是一个数组）
        PsiClass[] classes = javaFile.getClasses();
        if (0 == classes.length) {
            return null;
        }
        containerDirectory = classes[0].getContainingFile().getContainingDirectory();
        return classes[0];
    }
}
