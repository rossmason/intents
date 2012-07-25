<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
        exclude-result-prefixes="doc" >

    <!-- the identity template copies everything 1:1 -->
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- this template explicitly cares for namespace'd attributes -->
    <xsl:template match="@doc:*">
        <!--<xsl:attribute name="{local-name()}">-->
            <!--<xsl:value-of select="."/>-->
        <!--</xsl:attribute>-->
    </xsl:template>


</xsl:stylesheet>