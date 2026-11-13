
package com.example.presencemanager

import android.os.Bundle
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ActionMode
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : AppCompatActivity() {

    private var actionMode: ActionMode? = null

    private val cabCallback = object : ActionMode.Callback {

        override fun onCreateActionMode(
            mode: ActionMode,
            menu: Menu
        ): Boolean {
            mode.menuInflater.inflate(
                R.menu.menu_cab_dashboard,
                menu
            )
            mode.title = "Actions contextuelles"
            return true
        }

        override fun onPrepareActionMode(
            mode: ActionMode,
            menu: Menu
        ): Boolean = false

        override fun onActionItemClicked(
            mode: ActionMode,
            item: MenuItem
        ): Boolean {

            return when (item.itemId) {

                R.id.action_afficher -> {
                    AlertDialog.Builder(this@DashboardActivity)
                        .setTitle("CAB")
                        .setMessage("Action Afficher exécutée.")
                        .setPositiveButton("OK", null)
                        .show()
                    true
                }

                R.id.action_terminer -> {
                    mode.finish()
                    true
                }

                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        // Bouton CAB
        val btnCab = findViewById<Button>(R.id.btnCab)

        btnCab.setOnClickListener {
            if (actionMode == null) {
                actionMode = startSupportActionMode(cabCallback)
            }
        }

        // Bouton Pop-up
        val btnPopup = findViewById<Button>(R.id.btnPopup)

        btnPopup.setOnClickListener {

            val popupMenu = PopupMenu(this, btnPopup)

            popupMenu.menuInflater.inflate(
                R.menu.menu_popup_dashboard,
                popupMenu.menu
            )

            popupMenu.setOnMenuItemClickListener { item ->

                when (item.itemId) {

                    R.id.action_ouvrir_groupes -> {
                        startActivity(
                            Intent(
                                this,
                                GroupesActivity::class.java
                            )
                        )
                        true
                    }

                    R.id.action_informations -> {
                        AlertDialog.Builder(this)
                            .setTitle("PrésenceManager")
                            .setMessage(
                                "Menu contextuel flottant du tableau de bord."
                            )
                            .setPositiveButton("OK", null)
                            .show()
                        true
                    }

                    else -> false
                }
            }

            popupMenu.show()
        }

        // Gestion des espaces système
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.layoutDashboard)
        ) { v, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Récupération des cartes
        val cardGroupes = findViewById<View>(R.id.cardGroupes)
        val cardAppel = findViewById<View>(R.id.cardAppel)
        val cardHistorique = findViewById<View>(R.id.cardHistorique)
        val cardStatistiques = findViewById<View>(R.id.cardStatistiques)

        val imgProfil = findViewById<View>(R.id.imgProfil)

        // Navigation Groupes
        cardGroupes.setOnClickListener {
            startActivity(
                Intent(this, GroupesActivity::class.java)
            )
        }

        // Navigation Faire l'appel
        cardAppel.setOnClickListener {
            startActivity(
                Intent(this, AppelActivity::class.java)
            )
        }

        // Navigation Historique
        cardHistorique.setOnClickListener {
            startActivity(
                Intent(this, HistoriqueActivity::class.java)
            )
        }

        // Navigation Statistiques
        cardStatistiques.setOnClickListener {
            startActivity(
                Intent(this, StatistiquesActivity::class.java)
            )
        }

        // Bouton Profil / WhatsApp
        imgProfil.setOnClickListener {

            val intentWhatsApp =
                packageManager.getLaunchIntentForPackage("com.whatsapp")

            if (intentWhatsApp != null) {
                startActivity(intentWhatsApp)
            } else {
                Toast.makeText(
                    this,
                    "WhatsApp n'est pas installé",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
