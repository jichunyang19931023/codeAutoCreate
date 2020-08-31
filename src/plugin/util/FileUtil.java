package plugin.util;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;

public class FileUtil {

    public static void createFile(Project project, PsiDirectory workDir, String fileName, String packagePath, String content) {
        String[] packageArray = packagePath.split("\\.");
        PsiDirectory serviceDirectory = workDir;
        for (String packageStr : packageArray){
            if (!workDir.getVirtualFile().getPath().contains(packageStr)){
                // 如果目录不存在则创建
                if (serviceDirectory.findSubdirectory(packageStr) == null){
                    serviceDirectory = serviceDirectory.createSubdirectory(packageStr);
                }else{
                    serviceDirectory = serviceDirectory.findSubdirectory(packageStr);
                }
            }
        }

        PsiFile serviceFile = PsiFileFactory.getInstance(project).createFileFromText(fileName + ".java", JavaFileType.INSTANCE, content);
        serviceDirectory.add(serviceFile);
    }
}
