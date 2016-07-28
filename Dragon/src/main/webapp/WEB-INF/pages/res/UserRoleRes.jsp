<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

var userRoleRes = {

  titles: {
    createUser: '<fmt:message key="userRole.title.createUser" />',
    updateUser: '<fmt:message key="userRole.title.updateUser" />',
    deleteUser: '<fmt:message key="userRole.title.deleteUser" />',
    disableUser: '<fmt:message key="userRole.title.disableUser" />',
    enableUser: '<fmt:message key="userRole.title.enableUser" />'
  },

  fields: {
    username: '<fmt:message key="userRole.field.username" />',
    password: '<fmt:message key="userRole.field.password" />',
    passwordHint: '<fmt:message key="userRole.field.passwordHint" />',
    firstName: '<fmt:message key="userRole.field.firstName" />',
    lastName: '<fmt:message key="userRole.field.lastName" />',
    fullName: '<fmt:message key="userRole.field.fullName" />',
    email: '<fmt:message key="userRole.field.email" />',
    telephone: '<fmt:message key="userRole.field.telephone" />',
    telephone2: '<fmt:message key="userRole.field.telephone2" />',
    status: '<fmt:message key="userRole.field.status" />',
    enabled: '<fmt:message key="userRole.field.enabled" />',
    locked: '<fmt:message key="userRole.field.locked" />'
  }
};