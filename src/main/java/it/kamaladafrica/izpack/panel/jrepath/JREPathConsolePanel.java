package it.kamaladafrica.izpack.panel.jrepath;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.api.data.InstallData;
import com.izforge.izpack.api.resource.Messages;
import com.izforge.izpack.api.substitutor.VariableSubstitutor;
import com.izforge.izpack.core.os.RegistryDefaultHandler;
import com.izforge.izpack.installer.console.AbstractConsolePanel;
import com.izforge.izpack.installer.console.ConsolePanel;
import com.izforge.izpack.installer.panel.PanelView;
import com.izforge.izpack.panels.path.PathInputBase;
import com.izforge.izpack.util.Console;
import com.izforge.izpack.api.config.Options;

import java.util.Arrays;
import java.util.Properties;

/**
 * The JREPathPanel panel console helper class.
 *
 * @author Mounir El Hajj
 */
public class JREPathConsolePanel extends AbstractConsolePanel
{
    private InstallData installData;
    private final VariableSubstitutor variableSubstitutor;
    private final RegistryDefaultHandler handler;

    /**
     * Constructs a <tt>JREPathConsolePanelHelper</tt>.
     *
     * @param variableSubstitutor the variable substituter
     * @param handler             the registry handler
     * @param panel               the parent panel/view. May be {@code null}
     */
    public JREPathConsolePanel(VariableSubstitutor variableSubstitutor, RegistryDefaultHandler handler,
                               PanelView<ConsolePanel> panel, InstallData installData)
    {
        super(panel);
        this.handler = handler;
        this.installData = installData;
        this.variableSubstitutor = variableSubstitutor;
        JREPathPanelHelper.initialize(installData);
    }

    public boolean run(InstallData installData, Properties properties)
    {
        String strTargetPath = properties.getProperty(InstallData.INSTALL_PATH);
        if (strTargetPath == null || "".equals(strTargetPath.trim()))
        {
            System.err.println("Missing mandatory target path!");
            return false;
        }
        else
        {
            try
            {
                strTargetPath = variableSubstitutor.substitute(strTargetPath);
            }
            catch (Exception e)
            {
                // ignore
            }
            installData.setInstallPath(strTargetPath);
            return true;
        }
    }

    /**
     * Runs the panel using the specified console.
     *
     * @param installData the installation data
     * @param console     the console
     * @return <tt>true</tt> if the panel ran successfully, otherwise <tt>false</tt>
     */
    @Override
    public boolean run(InstallData installData, Console console)
    {
        printHeadLine(installData, console);

        String detectedJavaVersion = "";
        String defaultValue = JREPathPanelHelper.getDefaultJavaPath(installData, handler);

        if(JREPathPanelHelper.skipPanel(installData, defaultValue))
        {
            return true;
        }
        String strPath;
        boolean bKeepAsking = true;
        while (bKeepAsking)
        {
            Messages messages = installData.getMessages();
            strPath = console.promptLocation("Select JRE path [" + defaultValue + "] ", defaultValue);
            if (strPath == null)
            {
                return false;
            }
            strPath = strPath.trim();

            strPath = PathInputBase.normalizePath(strPath);
            detectedJavaVersion = JREPathPanelHelper.getCurrentJavaVersion(strPath, installData.getPlatform());

            String errorMessage = JREPathPanelHelper.validate(strPath, detectedJavaVersion, messages);
            if (!errorMessage.isEmpty())
            {
                if (errorMessage.endsWith("?"))
                {
                    errorMessage += "\n" + messages.get("JREPathPanel.badVersion4");
                    String strIn = console.prompt(errorMessage, (String)null);
                    if (strIn == null)
                    {
                        return false;
                    }
                    if (strIn != null && (strIn.equalsIgnoreCase("y") || strIn.equalsIgnoreCase("yes")))
                    {
                        bKeepAsking = false;
                    }
                }
                else
                {
                    console.println(messages.get("PathInputPanel.notValid"));
                }
            }
            else
            {
                bKeepAsking = false;
            }
            installData.setVariable(JREPathPanelHelper.JRE_PATH, strPath);
        }

        return promptEndPanel(installData, console);
    }

    @Override
    public boolean generateOptions(InstallData installData, Options options)
    {
        final String name =JREPathPanelHelper.JRE_PATH;
        options.add(name, installData.getVariable(name));
        options.addEmptyLine(name);
        options.putComment(name, Arrays.asList(getPanel().getPanelId()));
        return true;
    }

    @Override
    public void createInstallationRecord(IXMLElement panelRoot)
    {
        new JREPathPanelAutomationHelper().createInstallationRecord(installData, panelRoot);
    }
}
