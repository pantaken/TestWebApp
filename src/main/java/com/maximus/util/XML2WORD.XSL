<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet 
	version="1.0"
	xmlns:w="http://schemas.microsoft.com/office/word/2003/wordml" 
	xmlns:v="urn:schemas-microsoft-com:vml" 
	xmlns:w10="urn:schemas-microsoft-com:office:word" 
	xmlns:sl="http://schemas.microsoft.com/schemaLibrary/2003/core" 
	xmlns:aml="http://schemas.microsoft.com/aml/2001/core" 
	xmlns:wx="http://schemas.microsoft.com/office/word/2003/auxHint" 
	xmlns:o="urn:schemas-microsoft-com:office:office" 
	xmlns:dt="uuid:C2F41010-65B3-11d1-A29F-00AA00C14882"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	exclude-result-prefixes="xsi">
	<xsl:variable name="dxaIndent" select="'360'" />
	<xsl:template match="/">
		<xsl:processing-instruction name="mso-application">progid="Word.Document"</xsl:processing-instruction>
		<xsl:variable name="currentSpacePreserve">
			<xsl:value-of select="/*/@xml:space"/>
		</xsl:variable>
		<w:wordDocument>
					<xsl:if test="$currentSpacePreserve != ''">
						<xsl:attribute name="xml:space">
							<xsl:value-of select="$currentSpacePreserve"/>
						</xsl:attribute>
					</xsl:if>
			<!-- save processing instructions as custom document properties in order to preserve them on save -->
			<xsl:if test="processing-instruction()">
				<o:CustomDocumentProperties>
					<xsl:element name="o:processingInstructions">
						<xsl:attribute name="dt:dt">string</xsl:attribute>
						<xsl:apply-templates select="processing-instruction()" mode="piMode" />
					</xsl:element>
				</o:CustomDocumentProperties>
			</xsl:if>
			<!-- check for schemaLocation attributes in the file - if they exist, convert them to schema library entries (so we can attach them if possible) -->
			<xsl:if test="//@xsi:schemaLocation">
				<sl:schemaLibrary>
					<xsl:for-each select="//@xsi:schemaLocation">
						<xsl:call-template name="schemaLocationTransform">
							<xsl:with-param name="schemaLocationString" select="." />
						</xsl:call-template>
					</xsl:for-each>
				</sl:schemaLibrary>
			</xsl:if>
			<!-- set Word document properties for raw XML - save as raw XML and show XML tags in the document -->
			<w:docPr>
				<w:view w:val="web" />
				<w:removeWordSchemaOnSave w:val="on" />
				<w:showXMLTags w:val="on" />
			</w:docPr>
			
			<!-- insert the body tags for the Word document, and begin processing the file's content -->
			<w:body>
				<xsl:apply-templates>
					<xsl:with-param name="spacePreserve" select="$currentSpacePreserve"/>
				</xsl:apply-templates>
			</w:body>
		</w:wordDocument>
	</xsl:template>
	<xsl:template match="*">
		<xsl:param name="spacePreserve" select="default"/>
		<!-- first, set the depth (in twips) for the current indent level -->
		<xsl:variable name="depth">
			<xsl:choose>
				<xsl:when test="count(ancestor::node()) * $dxaIndent >= 4320">
					<xsl:value-of select="4320" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$dxaIndent*count(ancestor::node()) - $dxaIndent" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="currentSpacePreserve">
			<xsl:choose>
				<xsl:when test="@xml:space">
					<xsl:value-of select="@xml:space" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$spacePreserve" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:copy>
			<xsl:copy-of select="@*" />
			<xsl:choose>
				<xsl:when test="count(node())=0">
					<!-- processing a leaf node with no children -->
					<w:p>
						<w:pPr>
							<w:ind>
								<xsl:attribute name="w:left">
									<xsl:value-of select="$depth" />
								</xsl:attribute>
							</w:ind>
						</w:pPr>
					</w:p>
				</xsl:when>
				<xsl:when test="count(node())=1">
					<!-- processing a node with one child node -->
					<xsl:choose>
						<xsl:when test="generate-id(child::node()[1])=generate-id(child::text()[1])">
							<!-- if the first node is a text node, their id's will be the same, therefore this node has a single child, and it's text -->
							<xsl:apply-templates mode="leafNode">
								<xsl:with-param name="spacePreserve" select="$currentSpacePreserve"/>
							</xsl:apply-templates>
						</xsl:when>
						<xsl:otherwise>
							<!-- otherwise, this node has a single child, and it's NOT text -->
							<w:p>
								<w:pPr>
									<w:ind>
										<xsl:attribute name="w:left">
											<xsl:value-of select="$depth" />
										</xsl:attribute>
									</w:ind>
								</w:pPr>
							</w:p>
							<xsl:apply-templates>
								<xsl:with-param name="spacePreserve" select="$currentSpacePreserve"/>
							</xsl:apply-templates>
							<w:p>
								<w:pPr>
									<w:ind>
										<xsl:attribute name="w:left">
											<xsl:value-of select="$depth" />
										</xsl:attribute>
									</w:ind>
								</w:pPr>
							</w:p>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<!-- processing a node with multiple children -->
					<w:p>
						<w:pPr>
							<w:ind>
								<xsl:attribute name="w:left">
									<xsl:value-of select="$depth" />
								</xsl:attribute>
							</w:ind>
						</w:pPr>
					</w:p>
					<xsl:apply-templates>
						<xsl:with-param name="spacePreserve" select="$currentSpacePreserve"/>
					</xsl:apply-templates>
					<w:p>
						<w:pPr>
							<w:ind>
								<xsl:attribute name="w:left">
									<xsl:value-of select="$depth" />
								</xsl:attribute>
							</w:ind>
						</w:pPr>
					</w:p>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="w:* | v:* | w10:* | sl:* | aml:* | wx:* | o:* | dt:*">
		<xsl:param name="spacePreserve" select="default"/>
		<xsl:variable name="currentSpacePreserve">
			<xsl:choose>
				<xsl:when test="@xml:space">
					<xsl:value-of select="@xml:space" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$spacePreserve" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:apply-templates>
			<xsl:with-param name="spacePreserve" select="$currentSpacePreserve"/>
		</xsl:apply-templates>
	</xsl:template>
	
	<xsl:template match="text()">
		<xsl:param name="spacePreserve" select="default"/>
		<!-- first, set the depth (in twips) for the current indent level -->
		<xsl:variable name="depth">
			<xsl:choose>
				<xsl:when test="count(ancestor::node()) * $dxaIndent >= 4320">
					<xsl:value-of select="4320" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$dxaIndent*count(ancestor::node())-$dxaIndent" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="currentSpacePreserve"><xsl:value-of select="$spacePreserve" /></xsl:variable>
		<!-- processing a non-leaf text node, set the indent spacing -->
		<w:p>
			<w:pPr>
				<w:ind>
					<xsl:attribute name="w:left">
						<xsl:value-of select="$depth" />
					</xsl:attribute>
				</w:ind>
			</w:pPr>
			<w:r>
				<w:t>
					<xsl:if test="$currentSpacePreserve != ''">
						<xsl:attribute name="xml:space">
							<xsl:value-of select="$currentSpacePreserve"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="." />
				</w:t>
			</w:r>
		</w:p>
	</xsl:template>
	<xsl:template match="text()" mode="leafNode">
		<xsl:param name="spacePreserve" select="default"/>
		<!-- first, set the depth (in twips) for the current indent level -->
		<xsl:variable name="depth">
			<xsl:choose>
				<xsl:when test="count(ancestor::node()) * $dxaIndent >= 4320">
					<xsl:value-of select="4320" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$dxaIndent*count(ancestor::node()) - $dxaIndent * 2" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="currentSpacePreserve"><xsl:value-of select="$spacePreserve" /></xsl:variable>
		<!-- processing a leaf text node, set the indent spacing to account for the parent tag on the same line -->
		<w:p>
			<w:pPr>
				<w:ind>
					<xsl:attribute name="w:left">
						<xsl:value-of select="$depth" />
					</xsl:attribute>
				</w:ind>
			</w:pPr>
			<w:r>
				<w:t>
					<xsl:if test="$currentSpacePreserve != ''">
						<xsl:attribute name="xml:space">
							<xsl:value-of select="$currentSpacePreserve"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="." />
				</w:t>
			</w:r>
		</w:p>
	</xsl:template>
	<!-- we will copy the contents of PI's into custom document properties exactly as they were -->
	<xsl:template match="processing-instruction()" mode="piMode">&lt;?<xsl:value-of select="local-name()" />&#160;<xsl:value-of select="." />?&gt;</xsl:template>
	<!-- We will recursively call this function. It will go through each entry in the xsi:schemaLocation attribute -->
	<xsl:template name="schemaLocationTransform">
		<xsl:param name="schemaLocationString" />
		<xsl:variable name="schemaLocationStringNormalized">
			<xsl:value-of select="normalize-space($schemaLocationString)" />
		</xsl:variable>
		<xsl:variable name="schemaLocationStringAfterURI">
			<xsl:value-of select="substring-after($schemaLocationStringNormalized, ' ')" />
		</xsl:variable>
		<xsl:variable name="schemaLocationStringAfterLocation">
			<xsl:value-of select="substring-after($schemaLocationStringAfterURI, ' ')" />
		</xsl:variable>
		<sl:schema>
			<xsl:attribute name="sl:uri">
				<xsl:value-of select="substring-before($schemaLocationStringNormalized, ' ')" />
			</xsl:attribute>
			<xsl:attribute name="sl:schemaLocation">
				<xsl:if test="contains($schemaLocationStringAfterURI, ' ')">
					<xsl:value-of select="substring-before($schemaLocationStringAfterURI, ' ')" />
				</xsl:if>
				<xsl:if test="not(contains($schemaLocationStringAfterURI, ' '))">
					<xsl:value-of select="$schemaLocationStringAfterURI" />
				</xsl:if>
			</xsl:attribute>
		</sl:schema>
		<xsl:if test="contains($schemaLocationStringAfterLocation, ' ')">
			<xsl:call-template name="schemaLocationTransform">
				<xsl:with-param name="schemaLocationString" select="$schemaLocationStringAfterLocation" />
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
