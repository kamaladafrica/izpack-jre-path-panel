package it.kamaladafrica.izpack.panel.jrepath;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.api.adaptator.impl.XMLElementImpl;
import com.izforge.izpack.api.data.InstallData;
import com.izforge.izpack.api.data.Overrides;
import com.izforge.izpack.api.exception.InstallerException;
import com.izforge.izpack.installer.automation.PanelAutomation;
import com.izforge.izpack.installer.automation.PanelAutomationHelper;

public class JREPathPanelAutomationHelper extends PanelAutomationHelper implements PanelAutomation
{
    @Override
    public void createInstallationRecord(InstallData installData, IXMLElement rootElement)
    {
        String JREVarName = installData.getVariable("JREVarName");
        String JREPathName = installData.getVariable(JREVarName);

        IXMLElement JREPath = new XMLElementImpl("JREPath", rootElement);
        JREPath.setContent(JREPathName);
        rootElement.addChild(JREPath);

        IXMLElement JREVar = new XMLElementImpl("JREVarName", rootElement);
        JREVar.setContent(JREVarName);
        rootElement.addChild(JREVar);
    }

    @Override
    public void runAutomated(InstallData installData, IXMLElement panelRoot) throws InstallerException
    {
        IXMLElement JREPathElement = panelRoot.getFirstChildNamed("JREPath");
        String JREPath = JREPathElement.getContent();

        IXMLElement JREVarNameElement = panelRoot.getFirstChildNamed("JREVarName");
        String JREVarName = JREVarNameElement.getContent();

        installData.setVariable(JREVarName, JREPath);
    }

    @Override
    public void processOptions(InstallData installData, Overrides overrides) {}
}