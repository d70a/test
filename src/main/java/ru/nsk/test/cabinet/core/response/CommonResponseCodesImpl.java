package ru.nsk.test.cabinet.core.response;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import ru.nsk.test.cabinet.Mappings;
import ru.nsk.test.cabinet.ex.AlreadyRegisteredException;
import ru.nsk.test.cabinet.ex.LoginInvalidAttemptException;
import ru.nsk.test.cabinet.ex.SessionExpiredException;

/**
 * Common response codes supported by our application.
 *
 * @author me
 */
// TODO too big, split to separate classes implemented CommonResponseCodes
// and load these class by classpath scan
public enum CommonResponseCodesImpl implements CommonResponseCodes {

    SUCCESS() {
        @Override
        public String code() {
            return name();
        }

        @Override
        public String description() {
            return "SUCCESS";
        }

        @Override
        public Class[] exception() {
            return null;
        }

        @Override
        public String action() {
            return Mappings.EMPTY;
        }

        @Override
        public HttpStatus status() {
            return HttpStatus.OK;
        }
    },
    UNKNOW_ERROR() {
        @Override
        public String code() {
            return name();
        }

        @Override
        public String description() {
            return "My lovely error, unknown. Something broke at server side...";
        }

        @Override
        public Class[] exception() {
            return null;
        }

        @Override
        public String action() {
            return Mappings.EMPTY;
        }
        
        @Override
        public HttpStatus status() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    },
    INVALID_LOGIN_ATTEMPT() {
        @Override
        public String code() {
            return name();
        }

        @Override
        public String description() {
            return "Login/password invalid";
        }

        @Override
        public Class[] exception() {
            return new Class[]{LoginInvalidAttemptException.class};
        }

        @Override
        public String action() {
            return Mappings.EMPTY;
        }
        
        @Override
        public HttpStatus status() {
            return HttpStatus.OK;
        }
    },
    RESOURCE_NOT_FOUND() {
        @Override
        public String code() {
            return name();
        }

        @Override
        public String description() {
            return "Requested resourse not found";
        }

        @Override
        public Class[] exception() {
            return new Class[]{EmptyResultDataAccessException.class};
        }

        @Override
        public String action() {
            return Mappings.EMPTY;
        }
        
        @Override
        public HttpStatus status() {
            return HttpStatus.NO_CONTENT;
        }
    },
    ALREADY_REGISTERED() {
        @Override
        public String code() {
            return name();
        }

        @Override
        public String description() {
            return "Client with same credentials already registered in system.";
        }

        @Override
        public Class[] exception() {
            return new Class[]{AlreadyRegisteredException.class};
        }

        @Override
        public String action() {
            return Mappings.EMPTY;
        }
        
        @Override
        public HttpStatus status() {
            return HttpStatus.CONFLICT;
        }
    },
    ALREADY_REGISTERED_EMAIL() {
        @Override
        public String code() {
            return name();
        }

        @Override
        public String description() {
            return "Client with same email already registered in system.";
        }

        @Override
        public Class[] exception() {
            return null;
        }

        @Override
        public String action() {
            return Mappings.EMPTY;
        }
        
        @Override
        public HttpStatus status() {
            return HttpStatus.CONFLICT;
        }
    },
    ALREADY_REGISTERED_PHONE() {
        @Override
        public String code() {
            return name();
        }

        @Override
        public String description() {
            return "Client with same phone already registered in system.";
        }

        @Override
        public Class[] exception() {
            return null;
        }

        @Override
        public String action() {
            return Mappings.EMPTY;
        }
        
        @Override
        public HttpStatus status() {
            return HttpStatus.CONFLICT;
        }
    },
    ALREADY_REGISTERED_IDCARD() {
        @Override
        public String code() {
            return name();
        }

        @Override
        public String description() {
            return "Client with same id card already registered in system.";
        }

        @Override
        public Class[] exception() {
            return null;
        }

        @Override
        public String action() {
            return Mappings.EMPTY;
        }
        
        @Override
        public HttpStatus status() {
            return HttpStatus.CONFLICT;
        }
    },
    SESSION_EXPIRED() {
        @Override
        public String code() {
            return name();
        }

        @Override
        public String description() {
            return "Client session expired, please login in system.";
        }

        @Override
        public Class[] exception() {
            return new Class[]{SessionExpiredException.class};
        }

        @Override
        public String action() {
            return Mappings.A_RELOGIN;
        }
        
        @Override
        public HttpStatus status() {
            return HttpStatus.OK;
        }
    }
}
