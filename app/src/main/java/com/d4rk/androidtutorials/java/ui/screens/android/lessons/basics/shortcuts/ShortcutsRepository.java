package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts;

import android.util.SparseArray;

import androidx.annotation.LayoutRes;

import com.d4rk.androidtutorials.java.R;

import java.util.Collections;
import java.util.List;

public final class ShortcutsRepository {

    private ShortcutsRepository() { /* Utility class. */ }

    // --- Public API ----------------------------------------------------------
    public static List<ShortcutItem> getShortcuts(@LayoutRes int layoutResId) {
        List<ShortcutItem> list = SHORTCUTS_BY_LAYOUT.get(layoutResId);
        return list != null ? list : Collections.emptyList();
    }

    // --- Implementation details ---------------------------------------------
    private static final SparseArray<List<ShortcutItem>> SHORTCUTS_BY_LAYOUT = new SparseArray<>();

    private static final List<ShortcutItem> GENERAL_SHORTCUTS = List.of(
            new ShortcutItem("Ctrl + S", R.string.save_all),
            new ShortcutItem("Ctrl + ALT + Y", R.string.synchronize),
            new ShortcutItem("Ctrl + Shift + F12", R.string.maximize),
            new ShortcutItem("Ctrl + Shift + F", R.string.add_to_favorites),
            new ShortcutItem("Ctrl + Shift + I", R.string.file_inspect),
            new ShortcutItem("Ctrl + `", R.string.quick_switch_theme),
            new ShortcutItem("Ctrl + Alt + S", R.string.open_settings),
            new ShortcutItem("Ctrl + Alt + Shift + S", R.string.open_project),
            new ShortcutItem("Ctrl + Tab", R.string.switch_tabs)
    );

    private static final List<ShortcutItem> BUILD_SHORTCUTS = List.of(
            new ShortcutItem("Ctrl + F9", R.string.build),
            new ShortcutItem("Shift + F10", R.string.build_and_run),
            new ShortcutItem("Ctrl + F10", R.string.apply_changes)
    );

    private static final List<ShortcutItem> DEBUGGING_SHORTCUTS = List.of(
            new ShortcutItem("Shift + F9", R.string.debug),
            new ShortcutItem("F8", R.string.step_over),
            new ShortcutItem("F7", R.string.step_into),
            new ShortcutItem("Shift + F7", R.string.smart_step_into),
            new ShortcutItem("Shift + F8", R.string.step_out),
            new ShortcutItem("Alt + F9", R.string.run_to_cursor),
            new ShortcutItem("Alt + F8", R.string.evaluate_expression),
            new ShortcutItem("F9", R.string.resume_program),
            new ShortcutItem("Ctrl + F8", R.string.toggle_breakpoint),
            new ShortcutItem("Ctrl + Shift + F8", R.string.view_breakpoints)
    );

    private static final List<ShortcutItem> CODE_SHORTCUTS = List.of(
            new ShortcutItem("Alt + Insert", R.string.generate_code),
            new ShortcutItem("Ctrl + O", R.string.override_methods),
            new ShortcutItem("Ctrl + I", R.string.implement_methods),
            new ShortcutItem("Ctrl + Alt + T", R.string.surround),
            new ShortcutItem("Ctrl + Y", R.string.delete_line),
            new ShortcutItem("Ctrl +", R.string.collapse_and_expand_code_block),
            new ShortcutItem("-/+", R.string.collapse_and_expand_all_code_block),
            new ShortcutItem("Ctrl + Shift +", R.string.duplicate_current_line),
            new ShortcutItem("-/+", R.string.basic_code_completion),
            new ShortcutItem("Ctrl + D", R.string.smart_code_completion)
    );

    private static final List<ShortcutItem> REFACTORING_SHORTCUTS = List.of(
            new ShortcutItem("F5", android.R.string.copy),
            new ShortcutItem("F6", R.string.move),
            new ShortcutItem("Alt + Delete", R.string.safe_delete),
            new ShortcutItem("Shift + F6", R.string.rename),
            new ShortcutItem("Ctrl + F6", R.string.change_signature),
            new ShortcutItem("Ctrl + Alt + N", R.string.inline),
            new ShortcutItem("Ctrl + Alt + M", R.string.extract_method),
            new ShortcutItem("Ctrl + Alt + V", R.string.extract_variable),
            new ShortcutItem("Ctrl + Alt + F", R.string.extract_field),
            new ShortcutItem("Ctrl + Alt + C", R.string.extract_constant)
    );

    private static final List<ShortcutItem> VERSION_CONTROL_SHORTCUTS = List.of(
            new ShortcutItem("Ctrl + K", R.string.commit_project),
            new ShortcutItem("Ctrl + T", R.string.update_project),
            new ShortcutItem("Alt + Shift + C", R.string.view_recent_changes),
            new ShortcutItem("Alt + `", R.string.open_vcs_popup)
    );

    private static final List<ShortcutItem> NAVIGATION_SHORTCUTS = List.of(
            new ShortcutItem("Press Shift twice", R.string.search_everything),
            new ShortcutItem("Ctrl + F", R.string.find),
            new ShortcutItem("F3", R.string.find_next),
            new ShortcutItem("Shift + F3", R.string.find_previous),
            new ShortcutItem("Ctrl + R", R.string.replace),
            new ShortcutItem("Ctrl + Shift + A", R.string.find_action),
            new ShortcutItem("Ctrl + Alt + Shift + N", R.string.search_by_symbol_name),
            new ShortcutItem("Ctrl + N", R.string.find_class),
            new ShortcutItem("Ctrl + Shift + N", R.string.find_file),
            new ShortcutItem("Ctrl + Shift + Alt + N", R.string.find_path),
            new ShortcutItem("Ctrl + F12", R.string.open_file_structure),
            new ShortcutItem("Ctrl + Tab", R.string.navigate_between_open_tabs),
            new ShortcutItem("F4", R.string.jump_to_source),
            new ShortcutItem("Shift + F4", R.string.open_current_editor_tab_in_new_window),
            new ShortcutItem("Ctrl + E", R.string.recently_opened_files),
            new ShortcutItem("Ctrl + Shift + E", R.string.recently_edited_files),
            new ShortcutItem("Ctrl + Shift + Backspace", R.string.go_to_last_edit_location),
            new ShortcutItem("Ctrl + F4", R.string.close_active_editor_tabs),
            new ShortcutItem("Esc", R.string.return_to_editor_window),
            new ShortcutItem("Shift + Esc", R.string.hide_active_window),
            new ShortcutItem("Ctrl + G", R.string.go_to_line),
            new ShortcutItem("Ctrl + H", R.string.open_type_hierarchy),
            new ShortcutItem("Ctrl + Shift + H", R.string.open_v_hierarchy),
            new ShortcutItem("Ctrl + Alt + H", R.string.open_call_hierarchy)
    );

    static {
        SHORTCUTS_BY_LAYOUT.put(R.layout.activity_shortcuts_general, GENERAL_SHORTCUTS);
        SHORTCUTS_BY_LAYOUT.put(R.layout.activity_shortcuts_build, BUILD_SHORTCUTS);
        SHORTCUTS_BY_LAYOUT.put(R.layout.activity_shortcuts_debugging, DEBUGGING_SHORTCUTS);
        SHORTCUTS_BY_LAYOUT.put(R.layout.activity_shortcuts_code, CODE_SHORTCUTS);
        SHORTCUTS_BY_LAYOUT.put(R.layout.activity_shortcuts_refractoring, REFACTORING_SHORTCUTS);
        SHORTCUTS_BY_LAYOUT.put(R.layout.activity_shortcuts_version_control, VERSION_CONTROL_SHORTCUTS);
        SHORTCUTS_BY_LAYOUT.put(R.layout.activity_shortcuts_navigation_and_searching, NAVIGATION_SHORTCUTS);
    }
}
