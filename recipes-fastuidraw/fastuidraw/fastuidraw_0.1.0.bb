DESCRIPTION = "FastUIDraw"
SECTION = "examples"
LICENSE = "MPL"
LIC_FILES_CHKSUM = "file://COPYING;md5=86579064543cc51a54c883f5735ebf83"

#FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

inherit pkgconfig

# @TODO DEPEND on virtual/libgl virtual/libgles2 conditionally
DEPENDS = "freetype flex-native virtual/libgl virtual/libgles2"

# for the demos
DEPENDS += "fontconfig libsdl2 libsdl2-image"

SRCREV = "${AUTOREV}"

#SRC_URI = "git://github.com/intel/fastuidraw.git"
SRC_URI = "git://github.com/likewise/fastuidraw.git"

#SRC_URI += "file://fix-native-tools.patch"

S = "${WORKDIR}/git"

# @TODO set BUILD_GL(ES) variables depending on PACKAGECONFIG
# -e tells make to read environment settings
EXTRA_OEMAKE='BUILD_GL=0 BUILD_GLES=1 GL_DEFAULT_INCLUDEPATH=${RECIPE_SYSROOT}/usr/include LEX=flex INSTALL_LOCATION=${D}/usr LDFLAGS="${TARGET_LDFLAGS}"'

#EXTRA_OEMAKE = '-e MAKEFLAGS= CC="${CC} ${CFLAGS}" CXX="${CXX} ${CXXFLAGS}" LINK_LDFLAGS="${TARGET_LDFLAGS}"'
#EXTRA_OEMAKE += 'BUILD_GL=0 BUILD_GLES=1 GL_DEFAULT_INCLUDEPATH=${RECIPE_SYSROOT}/usr/include LEX=flex INSTALL_LOCATION=${D}/usr'

do_compile() {
        oe_runmake check
        oe_runmake libs
}

do_install() {
        oe_runmake 'DESTDIR=${D}' install
        install -d ${D}${bindir}
#        install -m 0755 ${S}/painter-simple-test-GLES-debug ${D}${bindir}/
#        install -m 0755 ${S}/painter-test-GLES-debug ${D}${bindir}/

	for i in $(find ${D} -name "*.pc") ; do
		sed -i -e s:prefix=.*:prefix=/usr:g $i
	done

	cd ${D}${libdir}
	for i in $(find . -regex .*.so) ; do
		mv $i $i.0
		ln -snf $i.0 $i
	done
}

# @TODO build/ngl_/Rules.mk: make native tools with g++ rather than (cross) $CXX
# @TODO Use ./ in front of two native tools in Rules.mk of nglbuilder stuff
# @TODO Force LEX ?= flex seems not picked up? (lex is tried) LEX := flex works
 
