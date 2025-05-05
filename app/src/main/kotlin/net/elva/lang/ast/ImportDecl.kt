package net.elva.lang.ast

data class ImportDecl(val pkgName: String, val imports: List<ImportItem>) : TopLevelDecl()
data class ImportItem(val name: String, val alias: String? = null)