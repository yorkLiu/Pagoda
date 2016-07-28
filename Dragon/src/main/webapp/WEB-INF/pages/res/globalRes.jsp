<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

var globalRes = {

  

  fields: {
    creteDate: '<fmt:message key="global.field.createDate" />',
    lastUpdateDate: '<fmt:message key="global.field.lastUpdateDate" />',
    creator: '<fmt:message key="global.field.creator" />',
    lastUpdater: '<fmt:message key="global.field.lastUpdater" />'
  },

  buttons: {
    save: '<fmt:message key="global.button.save" />',
    cancel: '<fmt:message key="global.button.cancel" />',
    update: '<fmt:message key="global.button.update" />',
    delete: '<fmt:message key="global.button.delete" />',
    enable: '<fmt:message key="global.button.enable" />',
    disable: '<fmt:message key="global.button.disable" />'
  },

  exception: {
    title: '<fmt:message key="global.exception.title" />',
    message: '<fmt:message key="global.exception.message" />'
  }
};
