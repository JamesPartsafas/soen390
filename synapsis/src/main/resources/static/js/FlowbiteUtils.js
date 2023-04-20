const getModalInstance = (id, instances) => {
    if (instances.some((modalInstance) => modalInstance.id === id)) {
        return instances.find((modalInstance) => modalInstance.id === id);
    }
    return null;
};

function initModals() {
    const modalInstances = [];
    document.querySelectorAll('[data-modal-target]').forEach(function ($triggerEl) {
        const modalId = $triggerEl.getAttribute('data-modal-target');
        const $modalEl = document.getElementById(modalId);
        if ($modalEl) {
            const placement = $modalEl.getAttribute('data-modal-placement');
            const backdrop = $modalEl.getAttribute('data-modal-backdrop');
            if (!getModalInstance(modalId, modalInstances)) {
                modalInstances.push({
                    id: modalId,
                    object: new Modal($modalEl, {
                        placement: placement
                            ? placement
                            : 'center',
                        backdrop: backdrop ? backdrop : 'dynamic',
                    }),
                });
            }
        }
        else {
            console.error("Modal with id ".concat(modalId, " does not exist. Are you sure that the data-modal-target attribute points to the correct modal id?."));
        }
    });
    document.querySelectorAll('[data-modal-toggle]').forEach(function ($triggerEl) {
        const modalId = $triggerEl.getAttribute('data-modal-toggle');
        const $modalEl = document.getElementById(modalId);
        if ($modalEl) {
            const placement = $modalEl.getAttribute('data-modal-placement');
            const backdrop = $modalEl.getAttribute('data-modal-backdrop');
            let modal_1 = getModalInstance(modalId, modalInstances);
            if (!modal_1) {
                modal_1 = {
                    id: modalId,
                    object: new Modal($modalEl, {
                        placement: placement
                            ? placement
                            : 'center',
                        backdrop: backdrop ? backdrop : 'dynamic',
                    }),
                };
                modalInstances.push(modal_1);
            }
            $triggerEl.addEventListener('click', function () {
                modal_1.object.toggle();
            });
        }
        else {
            console.error("Modal with id ".concat(modalId, " does not exist. Are you sure that the data-modal-toggle attribute points to the correct modal id?"));
        }
    });
    document.querySelectorAll('[data-modal-show]').forEach(function ($triggerEl) {
        const modalId = $triggerEl.getAttribute('data-modal-show');
        const $modalEl = document.getElementById(modalId);
        if ($modalEl) {
            const modal_2 = getModalInstance(modalId, modalInstances);
            if (modal_2) {
                $triggerEl.addEventListener('click', function () {
                    if (modal_2.object.isHidden) {
                        modal_2.object.show();
                    }
                });
            }
            else {
                console.error("Modal with id ".concat(modalId, " has not been initialized. Please initialize it using the data-modal-target attribute."));
            }
        }
        else {
            console.error("Modal with id ".concat(modalId, " does not exist. Are you sure that the data-modal-show attribute points to the correct modal id?"));
        }
    });
    document.querySelectorAll('[data-modal-hide]').forEach(function ($triggerEl) {
        const modalId = $triggerEl.getAttribute('data-modal-hide');
        const $modalEl = document.getElementById(modalId);
        if ($modalEl) {
            const modal_3 = getModalInstance(modalId, modalInstances);
            if (modal_3) {
                $triggerEl.addEventListener('click', function () {
                    if (modal_3.object.isVisible) {
                        modal_3.object.hide();
                    }
                });
            }
            else {
                console.error("Modal with id ".concat(modalId, " has not been initialized. Please initialize it using the data-modal-target attribute."));
            }
        }
        else {
            console.error("Modal with id ".concat(modalId, " does not exist. Are you sure that the data-modal-hide attribute points to the correct modal id?"));
        }
    });
}

function initPopovers() {
    document.querySelectorAll('[data-popover-target]').forEach(function ($triggerEl) {
        const popoverID = $triggerEl.getAttribute('data-popover-target');
        const $popoverEl = document.getElementById(popoverID);
        if ($popoverEl) {
            const triggerType = $triggerEl.getAttribute('data-popover-trigger');
            const placement = $triggerEl.getAttribute('data-popover-placement');
            const offset = $triggerEl.getAttribute('data-popover-offset');
            new Popover($popoverEl, $triggerEl, {
                placement: placement ? placement : 'top',
                offset: offset ? parseInt(offset) : 10,
                triggerType: triggerType
                    ? triggerType
                    : 'hover',
            });
        }
        else {
            console.error("The popover element with id \"".concat(popoverID, "\" does not exist. Please check the data-popover-target attribute."));
        }
    });
}