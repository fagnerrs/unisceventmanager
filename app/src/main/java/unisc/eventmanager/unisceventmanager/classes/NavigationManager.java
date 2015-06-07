package unisc.eventmanager.unisceventmanager.classes;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;


import java.util.Stack;

import unisc.eventmanager.unisceventmanager.R;

/**
 * Created by FAGNER on 05/04/2015.
 */
public class NavigationManager {

    /*public static void Navigate(Activity context, Fragment fragment)
    {
        android.app.FragmentManager fm = context.getFragmentManager();
        FragmentTransaction _trans = fm.beginTransaction();
        _trans.addToBackStack(null);
        _trans.replace(R.id.FrameLayoutFragment, fragment);
        _trans.commit();
    }*/

    private static FragmentManager m_FragmentManager = null;
    private static Stack<Fragment> _pilhaDeNavegacao = null;
    private static ActionBar m_ActionBar;

    public static void Navigate(Fragment fragment)
    {
        if (fragment.getClass().getName().equals("unisc.eventmanager.unisceventmanager.fragments.EventFragment"))
        {
            m_ActionBar.setDisplayHomeAsUpEnabled(false);
        }
        else
        {
            m_ActionBar.setDisplayHomeAsUpEnabled(true);

        }

            FragmentTransaction ft = m_FragmentManager.beginTransaction();
        //ft.replace(R.id.FrameLayoutMain, fragment);
        ft.commit();

        _pilhaDeNavegacao.push(fragment);
    }

    public static boolean Back()
    {
        Fragment _frag = _pilhaDeNavegacao.peek();

        if (!_frag.getClass().getName().equals("unisc.eventmanager.unisceventmanager.fragments.EventFragment"))
        {
            _pilhaDeNavegacao.pop();
            Fragment _frg = _pilhaDeNavegacao.pop();
            Navigate(_frg);

            return false;
        }
        else
        {
            return true;
        }
    }

    public static void Initialize(FragmentManager fragmentManager, ActionBar actionBar) {
        m_FragmentManager = fragmentManager;
        _pilhaDeNavegacao = new Stack<Fragment>();
        m_ActionBar = actionBar;
    }
}
