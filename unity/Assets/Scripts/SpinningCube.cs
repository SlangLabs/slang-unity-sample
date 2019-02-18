using UnityEngine;
using System;

public class SpinningCube : MonoBehaviour
{
    public float m_Speed = 20f, m_CachedSpeed = 20f;

    private Vector3 m_RotationDirection = Vector3.up;

    public void ToggleRotationDirection()
    {
        Debug.Log("Toggling rotation direction");

        if (m_RotationDirection == Vector3.up)
        {
            m_RotationDirection = Vector3.down;
        }
        else
        {
            m_RotationDirection = Vector3.up;
        }
    }

    public void StopRotation()
    {
        Debug.Log("Stopping rotation");

        m_CachedSpeed = m_Speed > 0 ? m_Speed : m_CachedSpeed;
        m_Speed = 0.0f;
    }

    public void StartRotation()
    {
        Debug.Log("Starting rotation");

        m_Speed = m_Speed <= 0.0f ? m_CachedSpeed : m_Speed;
    }

    public void SetRotationSpeed(string speedstr)
    {
        Debug.Log("Setting rotation speed to: " + speedstr);

        m_Speed = float.Parse(speedstr);
    }

    void Update()
    {
        transform.Rotate(m_RotationDirection * Time.deltaTime * m_Speed);
    }
}
