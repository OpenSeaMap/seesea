#!/bin/bash

# Variables
ZIP_FILE="./net.sf.seesea.postprocess.product/target/products/postprocessing-linux.gtk.x86_64.zip"
DEST_DIR="/app/postprocess"
CONF_DIR="/app/postprocess/config"

function patch_configfiles()
{
    echo "patch config files"
        
    sed -i "s|basedir=data|basedir=/app/data|g" ${CONF_DIR}/net.sf.seesea.content.impl.ContentDetector.cfg
    sed -i "s|basedir=data|basedir=/app/data|g" ${CONF_DIR}/net.sf.seesea.track.persistence.database.DatabaseTrackPersistence.cfg

    sed -i "s|password=changeme|password=\!2osm2\!|g" ${CONF_DIR}/net.sf.seesea.data.io.postgis.PostgresDatasourceConfiguration-depth.cfg
    sed -i "s|password=changeme|password=\!2osm2\!|g" ${CONF_DIR}/net.sf.seesea.data.io.postgis.PostgresDatasourceConfiguration-gauge.cfg
    sed -i "s|password=changeme|password=\!2osm2\!|g" ${CONF_DIR}/net.sf.seesea.data.io.postgis.PostgresDatasourceConfiguration-userdata.cfg

    sed -i "s|server=localhost|server=postgis|g" ${CONF_DIR}/net.sf.seesea.data.io.postgis.PostgresDatasourceConfiguration-depth.cfg
    sed -i "s|server=localhost|server=postgis|g" ${CONF_DIR}/net.sf.seesea.data.io.postgis.PostgresDatasourceConfiguration-gauge.cfg
    sed -i "s|server=localhost|server=postgis|g" ${CONF_DIR}/net.sf.seesea.data.io.postgis.PostgresDatasourceConfiguration-userdata.cfg

}

# Create the destination directory if it doesn't exist
if [ ! -d "$DEST_DIR" ]; then
    echo "Creating destination directory: $DEST_DIR"
    mkdir -p "$DEST_DIR"
fi

# Check if the ZIP file exists
if [ -f "$ZIP_FILE" ]; then
    echo "Unpacking $ZIP_FILE to $DEST_DIR"
    unzip -o "$ZIP_FILE" -d "$DEST_DIR"
    if [ $? -eq 0 ]; then
        echo "Unpacking completed successfully."
        patch_configfiles
    else
        echo "Failed to unpack the ZIP file."
        exit 1
    fi

else
    echo "Error: File $ZIP_FILE does not exist."
    exit 1
fi




