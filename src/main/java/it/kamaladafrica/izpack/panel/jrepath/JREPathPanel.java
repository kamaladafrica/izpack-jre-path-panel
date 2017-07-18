package it.kamaladafrica.izpack.panel.jrepath;

import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.api.data.Panel;
import com.izforge.izpack.api.handler.AbstractUIHandler;
import com.izforge.izpack.api.resource.Resources;
import com.izforge.izpack.api.substitutor.VariableSubstitutor;
import com.izforge.izpack.core.os.RegistryDefaultHandler;
import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.log.Log;
import com.izforge.izpack.installer.data.GUIInstallData;
import com.izforge.izpack.installer.gui.InstallerFrame;
import com.izforge.izpack.panels.path.PathInputPanel;
import com.izforge.izpack.util.Platform;

/**
 * Panel which asks for the JDK path.
 *
 * @author Klaus Bartz
 */
public class JREPathPanel extends PathInputPanel implements HyperlinkListener
{
    private static final long serialVersionUID = 3257006553327810104L;

    private static final Logger logger = Logger.getLogger(JREPathPanel.class.getName());

    private final RegistryDefaultHandler handler;

    /**
     * Constructs a <tt>JREPathPanel</tt>.
     *
     * @param panel       the panel meta-data
     * @param parent      the parent window
     * @param installData the installation data
     * @param resources   the resources
     * @param handler     the registry handler
     * @param replacer    the variable replacer
     * @param log         the log
     */
    public JREPathPanel(Panel panel, InstallerFrame parent, GUIInstallData installData, Resources resources,
                        RegistryDefaultHandler handler, VariableSubstitutor replacer, Log log)
    {
        super(panel, parent, installData, resources, log);
        this.handler = handler;

        setMustExist(true);
        if (!installData.getPlatform().isA(Platform.Name.MAC_OSX))
        {
            setExistFiles(JREPathPanelHelper.testFiles);
        }

        String msg = getString("JREPathPanel.JREDownload");
        if (msg != null && !msg.isEmpty())
        {
            add(IzPanelLayout.createParagraphGap());
            JEditorPane textArea = new JEditorPane("text/html; charset=utf-8", replacer.substitute(msg, null));
            textArea.setCaretPosition(0);
            textArea.setEditable(false);
            textArea.addHyperlinkListener(this);
            textArea.setBackground(getBackground());

            JScrollPane scroller = new JScrollPane(textArea);
            scroller.setAlignmentX(LEFT_ALIGNMENT);
            add(scroller, NEXT_LINE);
        }
        JREPathPanelHelper.initialize(installData);
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e)
    {
        try
        {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
            {
                String urls = e.getURL().toExternalForm();
                if (Desktop.isDesktopSupported())
                {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(new URI(urls));
                }
            }
        }
        catch (Exception err)
        {
            logger.log(Level.WARNING, err.getMessage());
        }
    }

    /**
     * Indicates wether the panel has been validated or not.
     *
     * @return Wether the panel has been validated or not.
     */
    @Override
    public boolean isValidated()
    {
        if(super.isValidated())
        {
            String detectedJavaVersion;
            String strPath = pathSelectionPanel.getPath();

            detectedJavaVersion = JREPathPanelHelper.getCurrentJavaVersion(strPath, installData.getPlatform());
            String errorMessage = JREPathPanelHelper.validate(strPath, detectedJavaVersion, installData.getMessages());
            if (!errorMessage.isEmpty())
            {
                if (errorMessage.endsWith("?"))
                {
                    if (askQuestion(getString("installer.warning"), errorMessage,
                            AbstractUIHandler.CHOICES_YES_NO,
                            AbstractUIHandler.ANSWER_NO) == AbstractUIHandler.ANSWER_YES)
                    {
                        this.installData.setVariable(JREPathPanelHelper.JRE_PATH, pathSelectionPanel.getPath());
                        return true;
                    }
                }
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Called when the panel becomes active.
     */
    @Override
    public void panelActivate()
    {
        super.panelActivate();
        String defaultValue = JREPathPanelHelper.getDefaultJavaPath(installData, handler);
        pathSelectionPanel.setPath(defaultValue);

        // Should we skip this panel?
        if(JREPathPanelHelper.skipPanel(installData, defaultValue))
        {
            parent.skipPanel();
        }
    }

    @Override
    public String getSummaryBody()
    {
        return this.installData.getVariable(JREPathPanelHelper.JRE_PATH);
    }

    @Override
    public void createInstallationRecord(IXMLElement panelRoot)
    {
        new JREPathPanelAutomationHelper().createInstallationRecord(installData, panelRoot);
    }

    @Override
    public void saveData()
    {
        this.installData.setVariable(JREPathPanelHelper.JRE_PATH, pathSelectionPanel.getPath());
    }
}
