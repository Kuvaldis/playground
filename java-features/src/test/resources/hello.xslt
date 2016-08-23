<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
    <xsl:template match="/hello-world">
        <html>
            <body>
                <h1>
                    <!-- value-of inserts the value calculated from expression in select-->
                    <xsl:value-of select="greeting"/>
                </h1>
                <!-- applies templates according to greeter template-->
                <xsl:apply-templates select="greeter"/>

                <xsl:apply-templates select="authors"/>
            </body>
        </html>
    </xsl:template>

    <!-- match defines what element to apply the template to -->
    <xsl:template match="greeter">
        <div>from <i><xsl:value-of select="."/></i></div>
    </xsl:template>

    <xsl:template match="authors">
        <table border="1">
            <tr>
                <th>Name</th>
            </tr>
            <!-- selects all authors with nationality 'Austrian' -->
            <!-- XPath is used here -->
            <xsl:for-each select="./author[nationality='Austrian']">
                <tr>
                    <td>
                        <!-- will print filtered authors' names-->
                        <xsl:value-of select="name" />
                    </td>
                </tr>
            </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>