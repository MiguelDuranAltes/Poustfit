import Swal from 'sweetalert2';
import router from '@/router'

const errorHandler = (err) => {

    if (err.response) {
        const status = err.response.status;
        const errorMessage = err.response.data.message;

        if (status === 401) {
            Swal.fire({
                title: 'Acceso no autorizado',
                text: errorMessage,
                icon: 'warning',
            });
            router.push("/welcome");
            return;
        }

        if (status === 403) {
            Swal.fire({
                title: 'Acceso denegado',
                text: errorMessage,
                icon: 'warning',
            });
            router.push("/welcome");
            return;
        }

        if (status === 409) {
            Swal.fire({
                title: 'Conflicto',
                text: errorMessage,
                icon: 'warning',
            });
            return;
        }

        if (status === 500) {
            Swal.fire({
                title: 'Error interno',
                text: errorMessage,
                icon: 'error',
            });
            return;
        }

    } else if (err.request) {
        console.error("No response received:", err.request);
        Swal.fire({
            title: 'Error de conexión',
            text: 'No se pudo conectar con el servidor. Revisa tu conexión a internet.',
            icon: 'warning',
        });
    } else {
        console.error("Unknown error:", err);
        Swal.fire({
            title: 'Error desconocido',
            text: 'Ocurrió un error inesperado.',
            icon: 'error',
        });
    }
};

export default errorHandler;
