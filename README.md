# izpack-jre-path-panel
JREPathPanel for IzPack: same as [JDKPathPanel](https://github.com/izpack/izpack/blob/master/izpack-panel/src/main/java/com/izforge/izpack/panels/jdkpath/JDKPathPanel.java) but for JRE.
The only difference between JREPathPanel and JDKPathPanel is that the JREPathPanel accept a JDK as choice too.

# Usage
The usage is the same as JDKPathPanel, so you can show the [JDKPathPanel official documentation](https://izpack.atlassian.net/wiki/spaces/IZPACK/pages/491644/JDKPathPanel).

You have only to keep in mind that all variables must be called with JRE prefix instead of JDK.

Es. 
***JDK**PathPanel.minVersion* become ***JRE**PathPanel.minVersion* and ***jdk**Path* become ***jre**Path*
